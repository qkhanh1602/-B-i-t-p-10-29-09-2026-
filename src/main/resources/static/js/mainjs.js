$(document).ready(function() {
    // Hiển thị thông tin người dùng đăng nhập thành công nếu đang ở trang profile
    if ($('#profile').length > 0) {
        $.ajax({
            type: 'GET',
            url: '/users/me',
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            beforeSend: function(xhr) {
                if (localStorage.token) {
                    xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.token);
                }
            },
            success: function(data) {
                var json = JSON.stringify(data, null, 4);
                $('#profile').html(data.fullName);
                if (data.images && document.getElementById("images")) {
                    document.getElementById("images").src = data.images;
                }
                console.log("SUCCESS : ", data);
            },
            error: function(e) {
                var json = e.responseText;
                if ($('#feedback').length > 0) {
                    $('#feedback').html(json);
                }
                console.log("ERROR : ", e);
                alert("Sorry, you are not logged in.");
                window.location.href = "/login";
            }
        });
    }

    // Hàm đăng xuất
    $('#logout').click(function() {
        localStorage.clear();
        window.location.href = "/login";
    });

    // Hàm Login
    $('#Login').click(function() {
        var email = document.getElementById('email').value;
        var password = document.getElementById('password').value;
        var basicInfo = JSON.stringify({
            email: email,
            password: password
        });

        $.ajax({
            type: "POST",
            url: "/auth/login",
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            data: basicInfo,
            success: function(data) {
                localStorage.token = data.token;
                window.location.href = "/user/profile";
            },
            error: function(e) {
                console.log("Login error: ", e);
                alert("Login Failed");
            }
        });
    });
});
