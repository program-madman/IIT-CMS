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
                selectionRole: 'role',
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
                        name: '',
                        children: [
                           
                        ],
                    },
                    {
                        id: 6,
                        name: '',
                        children: [
                            
                        ],
                    }
                ],
                //供应商已读列表
                hReads_sup: [
                   
                ],
                //供应商 已阅读 
                unReads_oem: [
                    {
                        id: 1,
                        name: 'OEM',
                        children: [
                          
                        ],
                    },
                ],
                hReads_oem: [
                   
                ],
                closeState: false,
                tabs: null,
                openTree: false,
                //选中数据
                allDistrictSelection: [],
                allDistrictItems: [

                   

                ],
                //选中数据
                vwDeptSelection: [],
                vwDeptItems: [


                   

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
                    this.arry = [];
                } else {
                    this.arry = [];
                }
            },
            //可见范围全选
            toItemSelectTree(isItemChecked){
                if (this.tabs===0){
                    if (isItemChecked) {
                        this.allDistrictSelection = [];
                    }else{
                        this.allDistrictSelection = [];
                    }  
                }else{
                    if (isItemChecked) {
                        this.vwDeptSelection = [];
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