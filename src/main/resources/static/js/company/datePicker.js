
$(function() {
    //input을 datepicker로 선언
    $("#datepicker1,#datepicker2").datepicker({
        dateFormat: 'yy-mm-dd' //달력 날짜 형태
        ,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
        ,showMonthAfterYear:true // 월- 년 순서가아닌 년도 - 월 순서
        ,changeYear: true //option값 년 선택 가능
        ,changeMonth: true //option값  월 선택 가능
        ,showOn: "both" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시
        ,buttonImage: "https://kr.seaicons.com/wp-content/uploads/2022/05/calendar-icon-1.png" //버튼 이미지 경로
        ,buttonImageOnly: true //버튼 이미지만 깔끔하게 보이게함
        ,buttonText: "선택" //버튼 호버 텍스트
        ,yearSuffix: "년" //달력의 년도 부분 뒤 텍스트
        ,monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 텍스트
        ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip
        ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 텍스트
        ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 Tooltip
        ,minDate: "-5Y" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
        ,maxDate: "+0d" //최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후)
    });

    //초기값을 오늘 날짜로 설정해줘야 합니다.
    $('#datepicker1,#datepicker2').datepicker('setDate', 'today'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, -1M:한달후, -1Y:일년후)
});



$(document).ready(function () {
    $("#summernote").summernote({
        height: 370,
        lang: "ko-KR",
        toolbar: [
            ["style", ["style"]],
            ["font", ["bold", "italic", "underline", "clear"]],
            ["fontname", ["fontname"]],
            ["color", ["color"]],
            ['insert', ['link', 'picture', 'video']],
            ["para", ["ul", "ol", "paragraph"]],
        ],
    });
});




    //결제 api
$(".pay-button-detailed").on("click", function () {
    IMP.init("imp00651153"); //가맹점 식별코드를 매개변수로 넘겨준다.

    IMP.request_pay(
        {
            pg: "kakaopay.TC0ONETIME", // kakaopay.{상점아이디(CID)}
            pay_method: "card", // 생략가
            merchant_uid: "merchant" + new Date().getTime(), // 상점에서 생성한 고유 주문번호
            name: "컨설팅 신청", // 상품 구매 이름
            amount: 3000, // 가격
            buyer_id: "구매자 아이디",
            pg_tid: "결제번호",
            m_redirect_url: "/shop/shoppingFinish", //결제 후 이동할 페이지 url(리다이렉트)
        },
    function (rsp) {
        // callback
        if (rsp.success) {
        // 빌링키 발급 성공

        console.log(rsp);
        console.log(rsp.buyer_id);
        console.log(rsp.name); //상품 이름
        console.log(rsp.merchant_uid); // 주문 고유 번호
        console.log(rsp.amount); // 가격
        console.log(rsp.pg_tid); // 결제 고유 번호

            let dataObj = {
                companyNumber : $('input[name="companyNumber"]').val(),
                consultingRequestComment : $('#summernote').val(),
                consultingRequestFirstDate : $('input[name="consultingRequestFirstDate"]').val(),
                consultingRequestLastDate : $('input[name="consultingRequestLastDate"]').val()
            };

            console.log(dataObj);
        $.ajax({
            url : '/companies/settingThePeriods',
            type : 'post',
            data : JSON.stringify(dataObj),
            contentType : "application/json; charset=utf-8",
            success : function (){
                console.log("success");
                window.location.href = "/company/consultingFinish";
            }
        });
        } else {
            // 빌링키 발급 실패
        }
    }
    );
});





