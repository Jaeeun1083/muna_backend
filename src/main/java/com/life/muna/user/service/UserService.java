package com.life.muna.user.service;

import com.life.muna.auth.util.JwtTokenProvider;
import com.life.muna.auth.util.PasswordEncoder;
import com.life.muna.common.error.ErrorCode;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.common.util.EmailUtil;
import com.life.muna.common.util.ImageUtil;
import com.life.muna.common.util.MaskingUtil;
import com.life.muna.mcoin.domain.Mcoin;
import com.life.muna.mcoin.mapper.McoinMapper;
import com.life.muna.product.domain.Product;
import com.life.muna.product.mapper.ProductHistoryMapper;
import com.life.muna.user.domain.TempKey;
import com.life.muna.user.domain.User;
import com.life.muna.user.domain.enums.LoginType;
import com.life.muna.user.domain.enums.UserStatus;
import com.life.muna.user.dto.info.UserLevelResponse;
import com.life.muna.user.dto.fcm.FcmTokenRequest;
import com.life.muna.user.dto.modify.CheckPasswordRequest;
import com.life.muna.user.dto.modify.ModifyPasswordRequest;
import com.life.muna.user.dto.find.FindPasswordRequest;
import com.life.muna.user.dto.signIn.SignInRequest;
import com.life.muna.user.dto.signIn.SignInResponse;
import com.life.muna.user.dto.signOut.SignOutRequest;
import com.life.muna.user.dto.signUp.SignUpRequest;
import com.life.muna.user.dto.profile.UserProfileResponse;
import com.life.muna.user.dto.withdraw.WithDrawUserRequest;
import com.life.muna.user.mapper.TempKeyMapper;
import com.life.muna.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
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
    private final EmailUtil emailUtil;
    private final String passwordResetPath;

    private final JwtTokenProvider jwtTokenProvider;
    private final String defaultImage;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[0-9a-zA-Zㄱ-ㅎ가-힣]*$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\\\\$%^&*]).{8,20}$");

    public UserService(UserMapper userMapper, TempKeyMapper tempKeyMapper, McoinMapper mcoinMapper, ProductHistoryMapper productHistoryMapper, PasswordEncoder passwordEncoder, EmailUtil emailUtil,  @Value("${password.reset}") String passwordResetPath, JwtTokenProvider jwtTokenProvider, @Value("${image.default-path}") String imagePath) {
        this.userMapper = userMapper;
        this.tempKeyMapper = tempKeyMapper;
        this.mcoinMapper = mcoinMapper;
        this.productHistoryMapper = productHistoryMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailUtil = emailUtil;
        this.passwordResetPath = passwordResetPath;
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
        Mcoin mcoin = mcoinMapper.findByUserCode(findUser.getUserCode());
        return UserProfileResponse.of(findUser, mcoin);
    }

    public boolean updateFcmToken(String emailFromToken, FcmTokenRequest fcmTokenRequest) {
        User findUser = userMapper.findByEmail(emailFromToken)
                .filter(user -> !user.getUserStatus().equals(UserStatus.WD))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        int result = userMapper.saveFcmToken(findUser.getUserCode(), fcmTokenRequest.getFcmToken());
        return result == 1;
    }

    public boolean isSamePassword (String emailFromToken, CheckPasswordRequest checkPasswordRequest) {
        User findUser = userMapper.findByEmail(emailFromToken)
                .filter(user -> !user.getUserStatus().equals(UserStatus.WD))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        return findUser.getPassword().equals(passwordEncoder.encrypt(checkPasswordRequest.getPassword()));
    }

    public boolean modifyPassword(String emailFromToken, ModifyPasswordRequest modifyPasswordRequest) {
        User findUser = userMapper.findByEmail(emailFromToken)
                .filter(user -> !user.getUserStatus().equals(UserStatus.WD))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        checkPassword(findUser, modifyPasswordRequest.getBefore());
        String password = passwordEncoder.encrypt(modifyPasswordRequest.getAfter());
        int saved = userMapper.savePassword(findUser.getUserCode(), password);
        return saved == 1;
    }

    public String resetPassword(FindPasswordRequest findPasswordRequest) {
        User findUser = userMapper.findByPhone(findPasswordRequest.getPhone())
                .filter(user -> !user.getUserStatus().equals(UserStatus.WD))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        if (!findUser.getLoginType().equals(LoginType.EMAIL)) throw new BusinessException(ErrorCode.CANNOT_RESET_PASSWORD_USER);

        String email = findUser.getEmail();

        // HTML 템플릿 파일을 읽어와 변수 값을 적용
        String templateContent = emailUtil.readResourceFile(passwordResetPath);
        String name = findUser.getNickname(); // 예제를 위해 사용자 이름을 하드코딩

        String resetPassword = generateRandomString(10);
        String storePassword = passwordEncoder.encrypt(resetPassword);
        int saved = userMapper.savePassword(findUser.getUserCode(), storePassword);
        // 변수 값을 HTML 템플릿에 적용.
        String body = templateContent.replace("{name}", name).replace("${resetPassword}", resetPassword);

        // HTML 형식의 이메일 발송.
        emailUtil.sendHtmlEmail(email, "[MUNA] 비밀번호 초기화 ", body);
        return MaskingUtil.getMaskedEmail(email);
    }

    public String findUserEmail(String phone) {
        User findUser = userMapper.findByPhone(phone)
                .filter(user -> !user.getUserStatus().equals(UserStatus.WD))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        // 마스킹하여 리턴
        return MaskingUtil.getMaskedEmail(findUser.getEmail());
    }

    //TODO 구현 예정
    public boolean withDrawUser(String email, WithDrawUserRequest withDrawUserRequest) {
        return false;
    }

    private void checkPassword(User user, String password) {
        if (!user.getPassword().equals(passwordEncoder.encrypt(password))) {
            throw new BusinessException(ErrorCode.MISMATCH_PASSWORD);
        }
    }

    private String generateRandomString(int length) {
        // 랜덤 문자열에 포함될 문자들
        String upperChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerChars = upperChars.toLowerCase();
        String numberChars = "0123456789";
        String specialChars = "@!$&";

        // 각각의 문자열을 배열로 변환하여 랜덤으로 선택할 수 있도록 함
        char[] upperArray = upperChars.toCharArray();
        char[] lowerArray = lowerChars.toCharArray();
        char[] numberArray = numberChars.toCharArray();
        char[] specialArray = specialChars.toCharArray();

        // 랜덤으로 선택된 문자들을 저장할 StringBuilder
        StringBuilder randomStringBuilder = new StringBuilder(length);

        // 각 문자셋에서 랜덤으로 한 글자씩 선택하여 저장
        randomStringBuilder.append(upperArray[new SecureRandom().nextInt(upperArray.length)]);
        randomStringBuilder.append(lowerArray[new SecureRandom().nextInt(lowerArray.length)]);
        randomStringBuilder.append(numberArray[new SecureRandom().nextInt(numberArray.length)]);
        randomStringBuilder.append(specialArray[new SecureRandom().nextInt(specialArray.length)]);

        // 남은 글자 수만큼 모든 문자셋에서 랜덤으로 선택하여 저장
        int remainingLength = length - 4;
        for (int i = 0; i < remainingLength; i++) {
            String allChars = upperChars + lowerChars + numberChars + specialChars;
            char[] allArray = allChars.toCharArray();
            randomStringBuilder.append(allArray[new SecureRandom().nextInt(allArray.length)]);
        }

        // 문자열 섞기
        return shuffleString(randomStringBuilder.toString());
    }

    // 문자열 섞기 메소드
    private String shuffleString(String input) {
        List<Character> characters = new ArrayList<>();
        for (char c : input.toCharArray()) {
            characters.add(c);
        }
        Collections.shuffle(characters, new SecureRandom());
        StringBuilder shuffledString = new StringBuilder();
        for (char c : characters) {
            shuffledString.append(c);
        }
        return shuffledString.toString();
    }

}
