$(document).ready(function () { // 아이디 찾기 함수 
    $("#findIdButton").click(function () {
        var memail = $("#findIdEmail").val();
        console.log(memail);
        $.ajax({
            type: "GET",
            url: "/jspteam4/MemberFindController",
            data: { type: "findId", data: memail },
            dataType: "json",
            success: function (data) {
                if (data && data.id) {
                    $("#foundId").text(" 사용자 ID: " + data.id);
                } else {
                    $("#foundId").text("유효한 ID를 찾을 수 없습니다.");
                }
            },
            error: function () {
                $("#foundId").text("서버 오류가 발생했습니다.");
            }
        });
    });

    $("#findPwButton").click(function () { // 비밀번호 찾기 함수
        var mid = $("#findPwMid").val();
        var memail = $("#findPwEmail").val();
        $.ajax({
            type: "GET",
            url: "/jspteam4/MemberFindController",
            data: { type: "findPw", data1: mid ,  data2: memail },
            dataType: "json",
            success: function (data) {
                if (data && data.pwd) {
                    $("#foundPwd").text(" 사용자 비밀번호: " + data.pwd);
                } else {
                    $("#foundPwd").text("유효한 비밀번호를 찾을 수 없습니다.");
                }
            },
            error: function () {
                $("#foundPwd").text("서버 오류가 발생했습니다.");
            }
        });
    });
});
