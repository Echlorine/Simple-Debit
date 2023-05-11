var CONTEXT_PATH = "";
// var CONTEXT_PATH = "/debit";

/* 窗口大小设置 */
window.onresize = winResize;
var resizeInterval = setInterval(winResize, 300), OldWinWidth = 0;

function winResize(e) {
    var WinWidth = window.innerWidth || document.body.clientWidth;
    var BodyClass = "w1500";
    if (OldWinWidth != WinWidth) {
        if (WinWidth < 1500)
            BodyClass = "w1200";
        if (WinWidth < 1240)
            BodyClass = "w1000";
        $(document.body).attr("id", BodyClass);
    }
}

/* 月份下拉菜单点击事件 */
function queryByMonth() {
    //获取选中项的值
    var month = $("#selectMonth option:selected").attr("value");
    var date = new Date().getFullYear() + "-" + month + "-01";
    $.post(
        CONTEXT_PATH + "/queryByMonth",
        {"month":date},
        function(data) {
            data = $.parseJSON(data);
            $("#amount_month").html(data.amountMonth);
            // console.log(data)
        }
    );
}

/* 年份下拉菜单点击事件 */
function queryByYear() {
    //获取选中项的值
    var year = $("#selectYear option:selected").attr("value") + "-01-01";
    $.post(
        CONTEXT_PATH + "/queryByYear",
        {"year":year},
        function(data) {
            data = $.parseJSON(data);
            $("#amount_year").html(data.amountYear);
            // console.log(data)
        }
    );
}

/* 按钮点击事件 */
function addDebit() {
    // var amount = $("input[name='amount']").val();
    // var description = $("input[name='description']").val();
    var amount = $("#amount").val();
    var description = $("#description").val();
    var date = $("#todayDate").val();
    var month = $("#selectMonth option:selected").attr("value");
    var year = $("#selectYear option:selected").attr("value");
    if (amount== '' || description == '') {
        // console.log("需要输入值");
        return;
    }
    var dates = date.split("-");
    $.post(
        CONTEXT_PATH + "/add",
        {"amount":amount, "description":description, "day":date},
        function(data) {
            data = $.parseJSON(data);
            var str = "";
            $("#detail").html(str);
            if (data.detail.length == 0) {
                str += "<li>" + "暂无记录" +"</li>";
            }
            for (var i = 0; i < data.detail.length; i++) {
                str += "<li>" + data.detail[i] +"</li>";
            }
            $("#amount_day").html(data.amountDay);
            $("#detail").html(str);
            if (month == dates[1]) {
                $("#amount_month").html(data.amountMonth);
            }
            if (year == dates[0]) {
                $("#amount_year").html(data.amountYear);
            }
            /* 清空输入框 */
            $("#amount").val('');
            $("#description").val('');
            // console.log(data);
        }
    );
}

/* 日历点击事件 */
function FixRili_Table(sid) {
    var riliday = $("#" + sid + " .riliday");
    for (var i = 0; i < riliday.length; i++) {
        var o = riliday.eq(i);
        o.click(this_Riliday_click);
    }
}

function this_Riliday_click() {
    var o = $(this);
    var tag = ToInt(o.attr("tag"), 0);
    try {
        Run_Rili_JinCha_Click(tag);
    } catch (e) {}
}

function Run_Rili_JinCha_Click(tag) {
    var o = null, i = 0;
    for (i = 0; i < dayArr.length; i++) {
        if (ToInt(dayArr[i].tag, 0) == tag) {
            o = dayArr[i];
            break;
        }
    }
    if (o == null)
        return;
    
    var date = o.year + "-" + o.month + "-" + o.day;
    var month = $("#selectMonth option:selected").attr("value");
    var year = $("#selectYear option:selected").attr("value");
    $("#today_day").html(o.year + "年" + o.month + "月");
    $("#today_day_img").attr("src", CONTEXT_PATH + "/imgs/" + o.day + o.isWeekend + ".png");
    $("#today_week_id").html(o.weekOfDay);
    $("#today_date_id").attr("class", "date" + o.isWeekend);
    /* 清空输入框 */
    $("#amount").val('');
    $("#description").val('');
    $("#todayDate").attr("value", date);

    $.post(
        CONTEXT_PATH + "/queryAll",
        {"day":date},
        function(data) {
            data = $.parseJSON(data);
            var str = "";
            $("#detail").html(str);
            if (data.detail.length == 0) {
                str += "<li>" + "暂无记录" +"</li>";
            }
            for (var i = 0; i < data.detail.length; i++) {
                str += "<li>" + data.detail[i] +"</li>";
            }
            $("#amount_day").html(data.amountDay);
            if (o.month == month) {
                $("#amount_month").html(data.amountMonth);
            }
            if (o.year == year) {
                $("#amount_year").html(data.amountYear);
            }
            $("#detail").html(str);
            // console.log(data)
        }
    );
}

function ToInt(n, def) {
    return FixNum(parseInt(n.replace(/[^-\d]/, '')), def);
}

function FixNum(n, def) {
    if (typeof (n) != "number")
        return def;
    if (isNaN(n))
        return def;
    return n;
}