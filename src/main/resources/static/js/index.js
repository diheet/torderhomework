//전역변수 선언
var menuCart = new Array;
var test = new Array;
var payar = new Array;

//로그인X 일 때, 메뉴 클릭시 장바구니 넣기
$(document).ready(function () {
    if (!localStorage.getItem("token")) {
        alert("로그인 후 이용해주세요!")
        location.href = "/main/login"
    }
    console.log("테스트 입니다.");
    menuList();

    $(document).on("click", "div[class=foods]", function () {
        var inCode = $(this).children().eq(3).val();
        var countIndex = menuCart.findIndex(function (key) {
            return key.menu_code == inCode
        });

        if (countIndex != -1) {
            menuCart[countIndex].count = Number(menuCart[countIndex].count) + 1;
            showList();
        } else {
            var map = {};
            map["menu_name"] = $(this).children().eq(0).text();
            map["menu_code"] = $(this).children().eq(3).val();
            map["price"] = $(this).children().eq(1).text();
            map["count"] = 1;

            menuCart.push(map);

            showList();
        }
    });
});

//메뉴 목록
function menuList() {
    $.ajax({
        url: "/main/menu",
        type: "GET",
        success: function (data) {
            console.log(data);
            for (var i = 0; i < data.length; i++) {
                $('#menu').append('<div id="menu_' + i + '" class="foods" value="' + data[i].menu_code + '">'
                    + '<span id = "menu' + i + '">' + data[i].menu_name + '</span>'
                    + '<span id = "menu' + i + '">' + data[i].price + '</span>' + '<br>'
                    + '<input type="hidden" value=' + data[i].menu_code + '>'
                    + '</div>');
            }
        }, error: function (e) {
            console.log(e);
        }
    });
}

//장바구니 목록
function showList() {
    $('#cartin').empty();
    for (var i = 0; i < menuCart.length; i++) {
        $('#cartin').append(
            '<div>' + menuCart[i].menu_name + '</div>'
            + '<span>' + menuCart[i].count + '</span>'
            + '<button type="button" onclick="outCart(' + menuCart[i].menu_code + ')">삭제</button>'
        );
    }
}

//장바구니 메뉴 삭제
function outCart(num) {
    var listIndex = menuCart.findIndex(function (key) {
        return key.menu_code == num
    });
    if (listIndex != -1) {
        menuCart.splice(listIndex, 1);
    }
    showList();
}

//주문하기
function order() {
    console.log(menuCart);
    $.ajax({
        url: "/main/order",
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.getItem('token'));
        },
        contentType: 'application/json',
        data: JSON.stringify(menuCart),
        success: function (data) {
            console.log(data);
            $('#cartin').empty();
            menuCart = [];
        }, error: function (e) {
            console.log(e);
        }
    });
}

//주문목록
function getorderlist() {
    test = [];
    $.ajax({
        url: "/main/list",
        type: "GET",
        success: function (orderlist) {
            test = orderlist;
            $('#listorder').empty();
            for (var i = 0; i < orderlist.length; i++)
                $('#listorder').append(
                    '<div>' + orderlist[i].menu_name
                    + '<span>' + orderlist[i].count + '</span>'
                    + '</div>'
                );

        }, error: function (e) {
            console.log(e);
        }
    });
}

//결제하기
function paylist() {
    $.ajax({
        url: "/main/paylist",
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.getItem('token'));
        },
        contentType: 'application/json',
        data: JSON.stringify(test),
        success: function (payment) {
            console.log("결제 성공");
            $('#listorder').empty();
            test = [];

        }
    });
}

//결제 목록
function lastlist() {
    $.ajax({
        url:"/main/lastlist",
        type:"GET",
        success: function (getpaylist) {
            var totalPrice = 0;
            $('#paylist').empty();
            for (var i = 0; i < getpaylist.length; i++){
            totalPrice += getpaylist[i].count * getpaylist[i].price;
            console.log(getpaylist[0].menu_name);
                $('#paylist').append(
                    '<div> <span>' + getpaylist[i].menu_name + '</span>'
                    + '<span>' + getpaylist[i].count + '</span>'
                    + '<span class = "payPrice">' + getpaylist[i].price + '</span>'
                    + '</div>'
                    );
                }
                $('#paylist').append(
                '<span id = "totalTitle">총금액</span>'
                + '<span id = "totalPrice">' + totalPrice + '</span>'
                );
        }, error: function (e) {
            console.log(e);
        }
    })
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username')
    window.location.href = '/main/login';
}