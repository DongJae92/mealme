package com.example.mealme.service.user;

import com.example.mealme.dto.CompanyDto;
import com.example.mealme.dto.UserDto;
import com.example.mealme.mapper.UserMapper;
import com.example.mealme.util.MailUtil;
import com.example.mealme.util.Util;
import com.example.mealme.vo.CompanyModifyVo;
import com.example.mealme.vo.UserModifyVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserMapper userMapper;

    // 구글 이메일
    @Value("${google.id}")
    private String myEmail;
    // 구글 비번
    @Value("${google.pw}")
    private String myPw;

    // 회원 등록
    public void registerUser(UserDto userDto) {
        if (userDto == null) {
            throw new IllegalArgumentException("회원정보 누락 !");
        }

        String userPassword = Util.pwSha256(userDto.getUserPassword());
        userDto.setUserPassword(userPassword);
        System.out.println("userPassword ======= " + userPassword);
        userMapper.insertUser(userDto);
    }

    public void registerKakaoUser(UserDto userDto){
        if (userDto == null){
            throw new IllegalArgumentException("회원정보 누락 !");
        }
        String userPassword = Util.pwSha256(userDto.getUserPassword());
        userDto.setUserPassword(userPassword);
        System.out.println("userPassword ======= " + userPassword);
        userMapper.insertKakaoUser(userDto);
    }

    // 유저 아이디 중복검사
    public boolean checkUserId(String userId){
        if (userId == null){
            throw new IllegalArgumentException("userId 없음");
        }
        int count = userMapper.checkUserId(userId);

        return count > 0;
    }

    /**
     * 회원 번호 조회하기 (아이디, 패스워드)
     *
     * @param userId
     * @param userPassword
     * @return
     * @throws IllegalArgumentException 존재하지 않는 회원 id, pw로 조회하는 경우
     */
    @Transactional(readOnly = true)
    public Long findUserNumber(String userId, String userPassword) {
        if (userId == null || userPassword == null) {
            throw new IllegalArgumentException("유저 id, password 누락 !");
        }

        String userPw = Util.pwSha256(userPassword);

        return Optional.ofNullable(userMapper.selectUserNumber(userId, userPw))
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("존재하지 않는 개인회원입니다. ");
                });
    }

    // 기업회원등록
    public void registerCompany(CompanyDto companyDto) {
        if (companyDto == null) {
            throw new IllegalArgumentException("회원정보 누락 !");
        }

        String userPassword;
        userPassword = Util.pwSha256(companyDto.getCompanyPassword());
        companyDto.setCompanyPassword(userPassword);
        System.out.println("userPassword ======= " + userPassword);
        userMapper.insertCompany(companyDto);
    }

    // 유저 아이디 중복검사
    public boolean checkCompanyId(String companyId){
        if (companyId == null){
            throw new IllegalArgumentException("userId 없음");
        }
        int count = userMapper.checkCompanyId(companyId);

        return count > 0;
    }

    // 기업로그인
    @Transactional(readOnly = true)
    public Long findCompanyNumber(String companyId, String companyPassword){
        if (companyId == null || companyPassword == null){ throw new IllegalArgumentException("기업 id, password 누락!");}

        String companyPw = Util.pwSha256(companyPassword);

        return Optional.ofNullable(userMapper.selectCompanyNumber(companyId, companyPw))
                .orElseThrow(() -> {throw new IllegalArgumentException("존재하지 않는 기업회원입니다. ");});
    }

    public String getUserName(Long userNumber){
        if (userNumber == null){
            throw new IllegalArgumentException("userNumber 없음 !");
        }
        return Optional.ofNullable(userMapper.getUserName(userNumber))
                .orElseThrow(() -> {throw new IllegalArgumentException("존재하지 않는 회원" );});
    }

    public String getCompanyName(Long companyNumber){
        if (companyNumber == null){
            throw new IllegalArgumentException("userNumber 없음 !");
        }
        return Optional.ofNullable(userMapper.getCompanyName(companyNumber))
                .orElseThrow(() -> {throw new IllegalArgumentException("존재하지 않는 회원" );});
    }



    // 유저 회원정보 수정 체크
    @Transactional(readOnly = true)
    public String userCheck(Long userNumber, String userPassword){
        System.out.println("비밀번호 입력함");
        if (userNumber == null || userPassword == null){
            throw new IllegalArgumentException("세션의 userNumber, 받은 password 누락");
        }

        String userPw = Util.pwSha256(userPassword);

        return Optional.ofNullable(userMapper.checkUser(userNumber,userPw))
                .orElseThrow(() -> {throw new IllegalArgumentException("존재하지 않는 유저입니다.");});
    }

    // 기업 회원정보 수정 체크
    @Transactional(readOnly = true)
    public String companyCheck(Long companyNumber, String companyPassword){
        if (companyNumber == null || companyPassword == null){
            throw new IllegalArgumentException("세션의 userNumber, 받은 password 누락");
        }

        String companyPw = Util.pwSha256(companyPassword);

        return Optional.ofNullable(userMapper.checkCompany(companyNumber,companyPw))
                .orElseThrow(() -> {throw new IllegalArgumentException("존재하지 않는 유저입니다.");});
    }

    // 유저 회원정보 가져오기
    @Transactional(readOnly = true)
    public UserModifyVo findUser(Long userNumber){
        if (userNumber == null){
            throw new IllegalArgumentException("userNumber, userId 없음 !");
        }
        return Optional.ofNullable(userMapper.checkUser2(userNumber))
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("존재하지 않는 회원");
                });
    }

    // 기업 회원정보 가져오기
    @Transactional(readOnly = true)
    public CompanyModifyVo findCompany(Long companyNumber){
        if (companyNumber == null) {
            throw new IllegalArgumentException("companyNumber, companyId 없음 !");
        }
            return Optional.ofNullable(userMapper.checkCompany2(companyNumber))
                    .orElseThrow(() -> {
                        throw new IllegalArgumentException("존재하지 않는 회원!");
                    });
    }

    // 유저 닉네임 수정하기
    public void modifyNickname(Long userNumber, String userNickname){
        if (userNumber == null || userNickname == null){
            throw new IllegalArgumentException("userNumber, userNickname 없음 !");
        }
        userMapper.updateUserNickname(userNumber, userNickname);
    }

    // 유저 코멘트 수정하기
    public void modifyComment(Long userNumber, String userComment){
        if (userNumber == null || userComment == null){
            throw new IllegalArgumentException("userNumber, userComment 없음 !");
        }
        userMapper.updateUserComment(userNumber, userComment);
    }

    // 유저 나머지 수정하기
    public void modifyUser(UserDto userDto){
        if (userDto == null){
            throw new IllegalArgumentException("수정할 데이터 없음 !");
        }
        String userPassword = Util.pwSha256(userDto.getUserPassword());
        userDto.setUserPassword(userPassword);
        userMapper.updateUser(userDto);
    }


    // 기업 네임 수정하기
    public void modifyCompanyName(Long companyNumber, String companyName){
        if (companyNumber == null || companyName == null){
            throw new IllegalArgumentException("companyNumber, companyName 없음 !");
        }
        userMapper.updateCompanyName(companyNumber, companyName);
    }

    // 기업 코멘트 수정하기
    public void modifyCompanyComment(Long companyNumber, String companyComment){
        if (companyNumber == null || companyComment == null){
            throw new IllegalArgumentException("userNumber, userComment 없음 !");
        }
        userMapper.updateCompanyComment(companyNumber, companyComment);
    }

    // 기업 나머지 수정하기
    public void modifyCompany(CompanyDto companyDto){
        if (companyDto == null){
            throw new IllegalArgumentException("수정할 데이터 없음 !");
        }
        String userPassword = Util.pwSha256(companyDto.getCompanyPassword());
        companyDto.setCompanyPassword(userPassword);
        userMapper.updateCompany(companyDto);
    }

    public String findPassword(UserDto userDto) throws Exception{
        if (userDto == null){
            throw new IllegalArgumentException("입력받은 user정보가 없습니다.");
        }
        if (userMapper.findUserPassword(userDto)>0) {
            // 비밀번호 입력이 잘 되었을 시 !
            // 랜덤한 문자열의 코드 생성 !
            String randomCode = Util.generateRandomString();
            System.out.println(randomCode);
            // 메일 전송 !
            MailUtil.Send(userDto.getUserEmail(), randomCode, myEmail, myPw);
            System.out.println("메일 발송 완료 !");
            // 랜덤 코드로 비밀번호 변경 !
            String encryptionPw = Util.pwSha256(randomCode);
            userMapper.updatePassword(encryptionPw, userDto.getUserId());
            System.out.println("비밀번호 변경 완료 !");
            return "user/findPasswordOk";
        } else {
            return "user/findPassword";
        }
    }
}