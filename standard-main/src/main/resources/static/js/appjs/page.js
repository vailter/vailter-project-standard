const prefix = "/page/";
const exampleTable = $('#exampleTable');
// let selectedBrwOrdNos = [];
// let pkgTypes = [];

$(function () {
    layui.use('laydate', function () {
        const laydate = layui.laydate;
        let now = moment();

        // 当value设置为：now + ' HH:mm:ss'，只要有一个为 00 时，`清空` 无效
        // 解决：1.设置大于00的；2.在外层设置就可以
        let startDateIns = laydate.render({
            elem: '#startDate', //指定元素Id
            type: 'date',
            format: 'yyyy-MM-dd',
            // value: now + ' 00:00:00', // `清空`无效
            zIndex: 99999999,
            calendar: true,
            btns: ['now', 'confirm'],
            change: function (value, date, endDate) {
                $('#startDate').val(value);
            },
            done: function (value, date, endDate) {
            }
        });

        let endDateIns = laydate.render({
            elem: '#endDate', //指定元素Id
            type: 'date',
            format: 'yyyy-MM-dd',
            value: now.format('YYYY-MM-DD'),
            zIndex: 99999999,
            calendar: false,
            btns: ['now', 'confirm'],
            change: function (value, date, endDate) {
                $('#endDate').val(value);
            },
            done: function (value, date, endDate) {
            }
        });

        $("#startDate").val(now.format('YYYY-MM-DD'));

        $(document).on("click", ".btn-success", function () {
            let startDate = $("#startDate").val();
            let endDate = $("#endDate").val();
            if (startDate && endDate) {
                if (compareDate(startDate, endDate)) {
                    layer.msg("办单开始时间不能大于结束时间！");
                    return;
                }
            }
            refreshTable();
        });
        // 初始化
        load();
    });
});

function load() {
    exampleTable.bootstrapTable(
        {
            method: 'get', // 服务器数据的请求方式 get or post
            url: prefix + "query", // 服务器数据的加载地址
            // showRefresh: true,
            // showToggle: true,
            iconSize: 'outline',
            toolbar: '#toolbar',
            toolbarAlign: "right",
            showExport: true,  //是否显示导出按钮
            exportDataType: "all",  //basic', 'all', 'selected'.
            buttonsAlign: "right",  //按钮位置
            exportTypes: ['excel'],  //导出文件类型
            Icons: 'glyphicon-export',
            striped: true, // 设置为true会有隔行变色效果
            dataType: "json", // 服务器返回的数据类型
            // queryParamsType : "limit",
            // //设置为limit则会发送符合RESTFull格式的参数
            // singleSelect: false, // 设置为true将禁止多选
            clickToSelect: true,
            // checkboxHeader: false,
            // contentType : "application/x-www-form-urlencoded",
            //发送到服务器的数据编码类型
            pagination: true, // 设置为true会在底部显示分页条
            pageNumber: 1,
            pageSize: 10,
            pageList: "[10, 20, 50, 100]",
            smartDisplay: true,
            // paginationHAlign: "left",
            // paginationDetailHAlign: "right",
            //search : true, // 是否显示搜索框
            // showToggle:true,
            // strictSearch: true,
            showColumns: true, // 是否显示内容下拉框（选择显示的列）
            // detailView: true,
            // detailFormatter: "detailFormatter",
            sidePagination: "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
            queryParams: function (params) {
                return buildQryParam(params);
            },
            columns: buildColumn()
        });
}

function detailFormatter(index, row) {
    console.log(row);
}

/**
 * 时间比较
 * @param d1
 * @param d2
 * @returns {boolean}
 */
function compareDate(d1, d2) {
    return ((new Date(d1.replace(/-/g, "\/"))) > (new Date(d2.replace(/-/g, "\/"))));
}

function buildQryParam($params) {
    let startDate = $('#startDate').val();
    let endDate = $('#endDate').val();
    return {
        //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
        limit: $params.limit,
        offset: $params.offset,
        startDate: startDate,
        endDate: endDate
    };
}

function buildColumn() {
    return [
        {
            field: 'index',
            title: '序号'
        },
        {
            field: 'name',
            title: '姓名',
            // events: operateEvents,
            // formatter: function (value, row, index) {
            //     if (value > 0) {
            //         return '<a class="advanceCount">' + value + '</a>';
            //     }
            //     return value;
            // }
        },
        {
            field: 'age',
            title: '年龄',
            visible: true
        }
    ];
}

function refreshTable(pageNumber, pageSize) {
    selectedBrwOrdNos = [];
    if (!pageNumber) {
        pageNumber = 1;
    }
    if (!pageSize) {
        pageSize = 10;
    }
    exampleTable.bootstrapTable('refresh', {query: {pageNumber: pageNumber, pageSize: pageSize}});
}

window.operateEvents = {
    'click .advanceCount': function (e, value, row, index) {
        let type = 1;
        go_detail(row.drainageCode, type);
    },
    'click .normalCount': function (e, value, row, index) {
        let type = 0;
        go_detail(row.drainageCode, type);
    }
};

function go_detail(drainageCode, type) {
    let content = prefix + 'day/list/detail/page?drainageCode=' + drainageCode +
        "&type=" + type +
        "&startDate=" + $('#startDate').val() +
        "&endDate=" + $('#endDate').val();
    let title = (type === 1 ? '提前结清' : '正常还款') + '详情列表';

    let index = layer.open({
        type: 2,
        title: title,
        maxmin: true,
        shadeClose: true, // 点击遮罩关闭层
        area: ['1200px', '670px'],
        content: content, // iframe的url
        cancel: function (index, layero) {
            // let options = exampleTable.bootstrapTable('getOptions');
            // exampleTable.bootstrapTable('refresh', {pageNumber: options.pageNumber, pageSize: options.pageSize});
            layer.close(index);
        }
    });
    layer.full(index);
}