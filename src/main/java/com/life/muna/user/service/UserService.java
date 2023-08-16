package com.life.muna.user.service;

import com.life.muna.auth.util.JwtTokenProvider;
import com.life.muna.auth.util.PasswordEncoder;
import com.life.muna.common.error.ErrorCode;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.common.util.ImageUtil;
import com.life.muna.mcoin.mapper.McoinMapper;
import com.life.muna.product.domain.Product;
import com.life.muna.product.mapper.ProductHistoryMapper;
import com.life.muna.user.domain.TempKey;
import com.life.muna.user.domain.User;
import com.life.muna.user.domain.enums.UserStatus;
import com.life.muna.user.dto.info.UserLevelResponse;
import com.life.muna.user.dto.notice.FcmTokenRequest;
import com.life.muna.user.dto.signIn.SignInRequest;
import com.life.muna.user.dto.signIn.SignInResponse;
import com.life.muna.user.dto.signOut.SignOutRequest;
import com.life.muna.user.dto.signUp.SignUpRequest;
import com.life.muna.user.dto.profile.UserProfileResponse;
import com.life.muna.user.mapper.TempKeyMapper;
import com.life.muna.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.life.muna.user.domain.User.createDefaultImage;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final TempKeyMapper tempKeyMapper;
    private final McoinMapper mcoinMapper;

    private final ProductHistoryMapper productHistoryMapper;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final String defaultImage;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[0-9a-zA-Zㄱ-ㅎ가-힣]*$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\\\\$%^&*]).{8,20}$");

    public UserService(UserMapper userMapper, TempKeyMapper tempKeyMapper, McoinMapper mcoinMapper, ProductHistoryMapper productHistoryMapper, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, @Value("${image.default-path}") String imagePath) {
        this.userMapper = userMapper;
        this.tempKeyMapper = tempKeyMapper;
        this.mcoinMapper = mcoinMapper;
        this.productHistoryMapper = productHistoryMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.defaultImage = imagePath + "/profile.jpg";
    }


    public boolean isDuplicated(String field, String data) {
        // Pattern은 입력 받은 정규 표현식에 대해 유한 상태 머신을 만들기 때문에 인스턴스 생성 비용이 높으므로 클래스 초기화 과정에서 직접 생성해 캐싱 후 재사용한다.
        return switch (field) {
            case "email" -> {
                Matcher matcher = EMAIL_PATTERN.matcher(data);
                if(!matcher.matches()) throw new BusinessException(ErrorCode.INVALID_EMAIL);
                yield userMapper.existsByEmail(data);
            }
            case "nickname" -> {
                Matcher matcher = NICKNAME_PATTERN.matcher(data);
                if(!matcher.matches()) throw new BusinessException(ErrorCode.INVALID_NICKNAME);
                yield userMapper.existsByNickName(data);
            }
            case "phone" -> {
                Matcher matcher = PHONE_PATTERN.matcher(data);
                if(!matcher.matches()) throw new BusinessException(ErrorCode.INVALID_PHONE);
                yield userMapper.existsByPhone(data);
            }
            default -> throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        };
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean signUp(SignUpRequest signUpRequest, MultipartFile thumbnail) {
        boolean isExistsEmail = isDuplicated("email", signUpRequest.getEmail());
        if (isExistsEmail) throw new BusinessException(ErrorCode.ALREADY_EXISTS_EMAIL);

        boolean isExistsNickname = isDuplicated("nickname", signUpRequest.getNickname());
        if (isExistsNickname) throw new BusinessException(ErrorCode.ALREADY_EXISTS_NICKNAME);

        boolean isExistsPhone = isDuplicated("phone", signUpRequest.getPhone());
        if (isExistsPhone) throw new BusinessException(ErrorCode.ALREADY_EXISTS_PHONE);

        // 본인 인증한 회원인지 체크
        TempKey tempKey = tempKeyMapper.findByPhone(signUpRequest.getPhone())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_TEMPKEY));
        if (!tempKey.isVerified()) throw new BusinessException(ErrorCode.NOT_VERIFY_TEMPKEY);

        if (thumbnail == null || thumbnail.isEmpty()) {
            signUpRequest.setProfileImage(createDefaultImage(defaultImage));
        } else {
            signUpRequest.setProfileImage(ImageUtil.resizeThumbnail(thumbnail));
        }

        User user = signUpRequest.toEntity(signUpRequest, passwordEncoder);
        int signUpResult = userMapper.save(user);
        int mcoinSaved = mcoinMapper.save(user.getUserCode());
        if (signUpResult != 1 || mcoinSaved != 1) throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        return true;
    }

    public SignInResponse signIn(SignInRequest signInRequest) {
        User findUser = userMapper.findByEmail(signInRequest.getEmail())
                .filter(user -> !user.getUserStatus().equals(UserStatus.WD))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        if (findUser.getUserStatus().equals(UserStatus.SD)) throw new BusinessException(ErrorCode.SUSPENDED_USER);

        if(!findUser.getLoginType().getTypeName().equals(signInRequest.getLoginType())) throw new BusinessException(ErrorCode.MISMATCH_LOGIN_TYPE);

        checkPassword(findUser, signInRequest.getPassword());
        return new SignInResponse(findUser.getUserCode(), jwtTokenProvider.createToken(findUser.getEmail()));
    }

    public void signOut(SignOutRequest signOutRequest) {
        User findUser = userMapper.findByEmail(signOutRequest.getEmail())
                .filter(user -> !user.getUserStatus().equals(UserStatus.WD))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        if (findUser.getUserStatus().equals(UserStatus.SD)) throw new BusinessException(ErrorCode.SUSPENDED_USER);
        userMapper.saveFcmToken(findUser.getUserCode(), null);
    }

    public UserLevelResponse getMyLevel(String emailFromToken) {
        User findUser = userMapper.findByEmail(emailFromToken)
                .filter(user -> !user.getUserStatus().equals(UserStatus.WD))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        List<Product> productList = productHistoryMapper.findReceivedProductBySellerCode(findUser.getUserCode());
        if (productList != null) {
            return UserLevelResponse.of(findUser, productList.size());
        } else {
            return UserLevelResponse.of(findUser, 0);
        }
    }

    public UserProfileResponse getUserProfile(String emailFromToken) {
        User findUser = userMapper.findByEmail(emailFromToken)
                .filter(user -> !user.getUserStatus().equals(UserStatus.WD))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        return UserProfileResponse.of(findUser);
    }

    public boolean updateFcmToken(String emailFromToken, FcmTokenRequest fcmTokenRequest) {
        User findUser = userMapper.findByEmail(emailFromToken)
                .filter(user -> !user.getUserStatus().equals(UserStatus.WD))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        int result = userMapper.saveFcmToken(findUser.getUserCode(), fcmTokenRequest.getFcmToken());
        return result == 1;
    }

    private void checkPassword(User user, String password) {
        if (!user.getPassword().equals(passwordEncoder.encrypt(password))) {
            throw new BusinessException(ErrorCode.MISMATCH_PASSWORD);
        }
    }

}
