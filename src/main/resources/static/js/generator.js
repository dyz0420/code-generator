$(function () {
    $("#jqGrid").jqGrid({
        url: 'sys/generator/list',
        datatype: "json",
        colModel: [
            {label: '表名', name: 'tableName', width: 100, key: true},
            {label: '引擎', name: 'engine', width: 70},
            {label: '表备注', name: 'tableComment', width: 100},
            {label: '创建时间', name: 'createTime', width: 100}
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50, 100, 200],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
});

const vm = new Vue({
    el: '#rrapp',
    data: {
        formConnect: {
            ip: '127.0.0.1',
            port: '3306',
            username: 'root',
            password: '123456',
            dbName: '',
            tablePrefix: [],
            author: '',
            pack: ''
        },
        q: {
            tableName: null,
        },
        key: sessionStorage.getItem("key") ? sessionStorage.getItem("key") : null
    },
    methods: {
        query: function () {
            $.get("sys/generator/check", {uuid: vm.key}, function (res) {
                if (res.success === true) {
                    $("#jqGrid").jqGrid('setGridParam', {
                        postData: {
                            'tableName': vm.q.tableName
                        },
                        page: 1
                    }).trigger("reloadGrid");
                } else {
                    alert("请连接数据库");
                }
            })
        },
        getConnection: function () {
            $.ajax({
                url: "/sys/generator/connect",
                type: "GET",
                data: {
                    ip: vm.formConnect.ip,
                    port: vm.formConnect.port,
                    username: vm.formConnect.username,
                    password: vm.formConnect.password,
                    dbName: vm.formConnect.dbName,
                    tablePrefix: vm.formConnect.tablePrefix,
                    author: vm.formConnect.author,
                    pack: vm.formConnect.pack
                },
                success: function (res) {
                    if (res.code !== 0) {
                        alert("连接失败，请检查表单信息！");
                        return;
                    }
                    vm.key = res.connect;
                    sessionStorage.setItem("key", res.connect)
                    vm.query();
                    alert("连接成功！");
                },
                error: function (err) {
                    alert("连接失败，请检查表单信息！");
                }
            })
        },
        generator: function () {
            const tableNames = getSelectedRows();
            if (tableNames == null) {
                return;
            }
            $.get("sys/generator/check", {uuid: vm.key}, function (res) {
                if (res.success === true) {
                    location.href = "sys/generator/code?tables=" + tableNames.join();
                } else {
                    alert("请连接数据库");
                }
            })
        }
    }
});

