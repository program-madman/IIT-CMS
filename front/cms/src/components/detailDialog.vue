<template>
    <v-card class="pa-6" justify="center">
        <v-row>
            <span class="title">
                {{ dialogTitle }}
            </span>
            <v-spacer>
            </v-spacer>
            <v-btn icon @click="close" right>
                <v-icon>mdi-close</v-icon>
            </v-btn>
        </v-row>
        <v-row class="pa-0 ma-0" justify="center" align="center" dark>
            <v-col sm="4" cols="6" class="pa-0 mt-9" dense>
                <v-select class="pa-0 ma-0" v-model="selectionRole" :items="['供应商','OEM']" @change="selectRole" dense
                    solo></v-select>
            </v-col>
            <v-col sm="2" cols="4" class="pa-0 ml-4 mt-9">
                <v-switch class="pa-0 ma-0" v-model="switch1" :label="switch1?`${switchHread}`:`${switchUnread}`">
                </v-switch>
            </v-col>
            <v-col sm="2" cols="4" class="pa-0 mt-4">
                <v-checkbox v-show="!switch1" v-model="ifChecked" :label="`全选`" @change="toSelectTree(ifChecked)">
                </v-checkbox>
            </v-col>
            <v-spacer></v-spacer>
            <v-col class="pa-0 ma-0">
                <v-dialog v-model="closeState" transition="dialog-bottom-transition" overlay-color="grey darken-1"
                    overlay-opacity="0.8" max-width="790">
                    <template v-slot:activator="{ on, attrs }">
                        <v-btn v-bind="attrs" v-on="on" depressed color="primary" class="pa-0" v-show="!switch1"
                            :disabled='arry.length === 0' @click="sendMail(arry)">
                            发送
                        </v-btn>
                    </template>
                    <template>
                        <v-card class="pa-6">
                            <v-row>
                                <span class="title">
                                    可见范围
                                </span>
                                <v-spacer>
                                </v-spacer>
                                <v-btn icon @click="closeitem" right>
                                    <v-icon>mdi-close</v-icon>
                                </v-btn>
                            </v-row>
                            <v-tabs v-model="tabs" centered class="mt-6">
                                <v-tab>
                                    发送给经销商
                                </v-tab>
                                <v-tab>
                                    发送给大众内部
                                </v-tab>
                            </v-tabs>
                            <v-tabs-items v-model="tabs">
                                <v-tab-item>
                                    <v-row class="pa-4" align="center">
                                        <v-btn icon small @click="toOpenTree(openTree)" depressed class="ml-10">
                                            <v-icon color="Orange" v-if="openTree">
                                                mdi-menu-down
                                            </v-icon>
                                            <v-icon color="Orange" v-else>
                                                mdi-menu-up
                                            </v-icon>                                  
                                        </v-btn>
                                        <v-checkbox v-model="ifItemChecked_sup" :label="`全选`" @change="toItemSelectTree(ifItemChecked_sup)" class="ml-8">
                                        </v-checkbox>
                                        <v-btn  depressed color="primary" class="ml-8" @click="itemSupSure" :disabled='allDistrictSelection.length === 0'>确定</v-btn>
                                    </v-row>
                                    <v-card color="pa-4">
                                        <v-treeview class="pa-0 ml-n4 " v-model="allDistrictSelection"
                                            :items="allDistrictItems" selection-type="leaf" selectable return-object
                                            open-all ref="allDistrictTW">
                                            <template v-slot:prepend="{ item }">
                                                <v-icon v-if="item.children"
                                                    v-text="`mdi-${item.id === 1 ? 'home-variant' : 'folder-network'}`">
                                                </v-icon>
                                            </template>
                                        </v-treeview>
                                    </v-card>
                                </v-tab-item>

                                <v-tab-item>
                                    <v-row class="pa-4" align="center">
                                    <v-btn icon small @click="toOpenTree(openTree)" width="30px" class="ml-10">
                                        <v-icon color="Orange" v-if="openTree">
                                            mdi-menu-down
                                        </v-icon>
                                        <v-icon color="Orange" v-else>
                                            mdi-menu-up
                                        </v-icon>
                                    </v-btn>
                                    <v-checkbox v-model="ifItemChecked_oem" :label="`全选`" @change="toItemSelectTree(ifItemChecked_oem)" class="ml-8">
                                    </v-checkbox>
                                    <v-btn  depressed color="primary" class="ml-8" @click="itemOemSure" :disabled='vwDeptSelection.length === 0'>确定</v-btn>
                                </v-row>
                                    <v-treeview class="pa-4 " v-model="vwDeptSelection" :items="vwDeptItems"
                                        selection-type="leaf" selectable return-object open-all ref="vwDeptTW" @input="selectAll(vwDeptSelection)">
                                        <template v-slot:prepend="{ item }">
                                            <v-icon v-if="item.children"
                                                v-text="`mdi-${item.id === 1 ? 'home-variant' : 'folder-network'}`">
                                            </v-icon>
                                        </template>
                                    </v-treeview>
                                </v-tab-item>
                            </v-tabs-items>
                        </v-card>
                    </template>
                </v-dialog>
            </v-col>
        </v-row>
        <div class="pa-0 ma-0">
            <v-treeview v-if="!switch1" ref="allDistrictTW" v-model="arry" selectable return-object open-all
            selection-type="leaf" :items="unReads"></v-treeview>
            <v-list v-else dense disabled>
                <v-list-item v-for="(item, i) in hReads" :key="i">
                    <v-list-item-content>
                        <v-list-item-title class="body-2">{{ item.name }} {{ item.deptment }} {{ item.company }}
                        </v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
            </v-list>
        </div>
    </v-card>
</template>

<script>
    export default {
        name: 'detaildialog',

        props: {
            dialogTitle: {
                type: String,
                default: ''
            }
        },
        data() {
            return {
                //选择未读
                switch1: false,
                //默认选中角色
                selectionRole: '供应商',
                // 是否全选
                ifChecked: false,
                //选中数据
                arry: [],
                //默认未阅读
                switchUnread: '未读',
                switchHread: '已读',
                //显示的未阅读
                unReads: [],
                //显示的已阅读,
                hReads: [],
                //供应商 未阅读
                unReads_sup: [
                    {
                        id: 1,
                        name: '北京三里屯4s店_sup',
                        children: [
                            { id: 2, name: 'Jhon 销售总监' },
                            { id: 3, name: 'Jack 销售经理' },
                            { id: 4, name: 'Jim 销售经理' },
                            { id: 5, name: 'Mary 销售经理' },
                        ],
                    },
                    {
                        id: 6,
                        name: '北京西三旗4s店_sup',
                        children: [
                            { id: 7, name: 'Jhon 销售总监' },
                            { id: 8, name: 'Jack 销售经理' },
                            { id: 9, name: 'Jim 销售经理' },
                            { id: 10, name: 'Mary 销售经理' },
                            { id: 11, name: 'Mary 助理' },
                        ],
                    }
                ],
                //供应商已读列表
                hReads_sup: [
                    { name: 'Jhon', company: '北京三里屯4s店_sup', deptment: '销售总监' },
                    { name: 'Jim', company: '北京三里屯4s店_sup', deptment: '销售经理' },
                    { name: 'Mary', company: '北京三里屯4s店_sup', deptment: '助理' },
                ],
                //供应商 已阅读 
                unReads_oem: [
                    {
                        id: 1,
                        name: 'OEM',
                        children: [
                            { id: 2, name: 'Jhon 销售总监' },
                            { id: 3, name: 'Jack 销售经理' },
                            { id: 4, name: 'Jim 销售经理' },
                            { id: 5, name: 'Mary 销售经理' },
                            { id: 6, name: 'Jhon2 销售总监' },
                            { id: 7, name: 'Jack2 销售经理' },
                            { id: 8, name: 'Jim2 销售经理' },
                            { id: 9, name: 'Mary2 销售经理' }
                        ],
                    },
                ],
                hReads_oem: [
                    { name: 'Jhon', company: '北京三里屯4s店_oem', deptment: '销售总监' },
                    { name: 'Jim', company: '北京三里屯4s店_oem', deptment: '销售经理' },
                    { name: 'Mary', company: '北京三里屯4s店_oem', deptment: '助理' },
                ],
                closeState: false,
                tabs: null,
                openTree: false,
                //选中数据
                allDistrictSelection: [],
                allDistrictItems: [

                    { id: 1, name: '北区', children: [{ id: 2, name: '70101010  大连大昌合众汽车销售服务有限公司' }, { id: 3, name: '70101012  沈阳大昌合众汽车销售服务有限公司' }, { id: 4, name: '70101088  上海中进众旺汽车销售服务有限公司' }, { id: 5, name: '70101098  浙江捷通汽车有限公司' }] },
                    { id: 6, name: '中区', children: [{ id: 7, name: '70101056  温州之远汽车有限公司' }, { id: 8, name: '70101073  大连大昌合众汽车销售服务有限公司' }, { id: 9, name: '70101074  北京联众汽车销售服务有限公司' }] },
                    { id: 10, name: '南区', children: [{ id: 11, name: '70101089  济南祥和汽车销售服务有限公司' }] },

                ],
                //选中数据
                vwDeptSelection: [],
                vwDeptItems: [


                    { id: 1, name: '宾利', children: [{ id: 2, name: '集团物流部' }, { id: 3, name: '中央市场部' }, { id: 4, name: '品牌售后部' }, { id: 5, name: '售后服务部' },], },
                    { id: 6, name: '兰博基尼', children: [{ id: 7, name: '财务部' }, { id: 8, name: '经销商网络部' }, { id: 9, name: 'IT' },], },
                    { id: 10, name: '保时捷', children: [{ id: 11, name: '法务部' },], },
                    { id: 12, name: '奥迪', children: [{ id: 13, name: '法务部' }, { id: 14, name: '中央市场部' }, { id: 15, name: '品牌售后部' }, { id: 16, name: '售后服务部' },], },
                    { id: 17, name: '大众', children: [{ id: 18, name: '法务2部' }, { id: 19, name: '经销商网络2部' },], },

                ],
                //可见范围是否全选
                ifItemChecked_sup: false,
                ifItemChecked_oem: false,
            };
        },
        //加载前
        mounted() {
            this.unReads = this.unReads_sup;
            this.hReads = this.hReads_sup;
            console.log(this.dialogTitle);
        },

        //销毁前
        beforeDestroy() {
            this.switch1 = false
            this.selectionRole = '供应商';
            this.ifChecked = false;
            this.arry = [];
            this.unReads = [];
            this.hReads = [];
        },


        methods: {
            //关闭
            close() {
                console.log("......关闭按钮.......");
                this.$emit('closedChild', false);
            },
            //选择角色
            selectRole() {
                console.log(this.selectionRole);
                if (this.selectionRole === '供应商') {
                    this.unReads = this.unReads_sup;
                    this.hReads = this.hReads_sup;
                } else {
                    this.unReads = this.unReads_oem;
                    this.hReads = this.hReads_oem;
                }
            },
            //打开tree
            toOpenTree(openTree) {
                console.log(openTree);
                if (this.tabs === 0)
                    this.$refs.allDistrictTW.updateAll(openTree);
                else
                    this.$refs.vwDeptTW.updateAll(openTree);
                this.openTree = !openTree;
            },
            //是否全选
            toSelectTree(isChecked) {
                console.log(isChecked);
                if (isChecked) {                
                    this.arry = [{
                        "id": 2,
                        "name": "Jhon 销售总监"
                    },
                    {
                        "id": 3,
                        "name": "Jack 销售经理"
                    },
                    {
                        "id": 4,
                        "name": "Jim 销售经理"
                    },
                    {
                        "id": 5,
                        "name": "Mary 销售经理"
                    },
                    {
                        "id": 6,
                        "name": "Jhon2 销售总监"
                    },
                    {
                        "id": 7,
                        "name": "Jack2 销售经理"
                    },
                    {
                        "id": 8,
                        "name": "Jim2 销售经理"
                    },
                    {
                        "id": 9,
                        "name": "Mary2 销售经理"
                    }];
                } else {
                    this.arry = [];
                }
            },
            //可见范围全选
            toItemSelectTree(isItemChecked){
                if (this.tabs===0){
                    if (isItemChecked) {
                        this.allDistrictSelection = [{"id":2,"name":"70101010  大连大昌合众汽车销售服务有限公司"},{"id":3,"name":"70101012  沈阳大昌合众汽车销售服务有限公司"},{"id":4,"name":"70101088  上海中进众旺汽车销售服务有限公司"},{"id":5,"name":"70101098  浙江捷通汽车有限公司"},{"id":7,"name":"70101056  温州之远汽车有限公司"},{"id":8,"name":"70101073  大连大昌合众汽车销售服务有限公司"},{"id":9,"name":"70101074  北京联众汽车销售服务有限公司"},{"id":11,"name":"70101089  济南祥和汽车销售服务有限公司"}];
                    }else{
                        this.allDistrictSelection = [];
                    }  
                }else{
                    if (isItemChecked) {
                        this.vwDeptSelection = [{"id":2,"name":"集团物流部"},{"id":3,"name":"中央市场部"},{"id":4,"name":"品牌售后部"},{"id":5,"name":"售后服务部"},{"id":7,"name":"财务部"},{"id":8,"name":"经销商网络部"},{"id":9,"name":"IT"},{"id":11,"name":"法务部"},{"id":13,"name":"法务部"},{"id":14,"name":"中央市场部"},{"id":15,"name":"品牌售后部"},{"id":16,"name":"售后服务部"},{"id":18,"name":"法务2部"},{"id":19,"name":"经销商网络2部"}];
                    }else{
                        this.vwDeptSelection = [];
                    }  
                }
                  
            },
            //全选
            selectAll(arry) {
                console.log(arry);
            },
            //发送
            sendMail(arry) {
                console.log(arry);
                this.close();
                this.closeState = true;
            },
            //关闭可见范围
            closeitem(){
                this.closeState = false;
            },
            //可见范围--供应商 确定
            itemSupSure(){
                console.log(this.allDistrictSelection);
            },
            //可见范围--OEM 确定
            itemOemSure(){
                console.log(this.vwDeptSelection);
            }
        },
    };
</script>