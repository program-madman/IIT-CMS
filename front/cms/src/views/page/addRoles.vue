<template>
  <v-card
  width="100%"
  tile
  outlined
  >
    <v-text-field class="pt-4 px-2"
          v-model="search"
          label="输入想要搜索的角色名称"
          hide-details
          clearable
          outlined
          clear-icon="mdi-close-circle-outline"
      ></v-text-field>

    <v-card-text >
      <div class="ml-n2">
        点击角色进行添加
      </div>
      <v-treeview
          :items="items"
          :search="search"
          :filter="filter"
          v-model="selection"
          :open.sync="open"
          selection-type="leaf"
          selectable
          return-object
          open-all
          @input="roleSelect"
          dense
          class="ml-n12"
      >
        <template v-slot:prepend="{ item }">
          <v-icon
              v-if="item.children"
              v-text="`mdi-${item.id === 1 ? 'human-male' : 'human-female'}`"
          ></v-icon>
        </template>
      </v-treeview>
    </v-card-text>
  </v-card>
</template>

<script>

export default {
  name: "addRoles",
  props: {
    roles: String,
  },
  data: () => ({
    items: [
      {
        id: 1,
        name: 'CMS Admin',
      },
      {
        id: 2,
        name: 'Admin',
      },
      {
        id: 3,
        name: '发布经理',
      },
      {
        id: 4,
        name: '网络部主管',
      },
      {
        id: 5,
        name: '网络部经理',
      },
      {
        id: 6,
        name: '售后服务经理',
      },
      {
        id: 7,
        name: '北区消息发布经理',
      },
      {
        id: 8,
        name: '中区消息发布经理',
      },
      {
        id: 9,
        name: '南区消息发布经理',
      },      {
        id: 10,
        name: '销售经理',
      },      {
        id: 11,
        name: '销售主管',
      },      {
        id: 12,
        name: '售后经理',
      },      {
        id: 13,
        name: '售后主管',
      },      {
        id: 14,
        name: '区域经理',
      },
    ],
      open: [1, 2],
      search: null,
      selection: [],

  }),
  methods:{

    //角色treeView，input时，向父组件传递选择项参数，激活父组件方法$emit.on
    roleSelect() {
      this.$emit('selectRoles',this.selection);
  },
  changeTree(content) {
    let temp = content.split(",");
    let that = this;
    that.selection=[];
    this.items.forEach(function (item){
      for (let tempItem of temp){
        if(tempItem===item.name)
        {
          that.selection.push({id:item.id,name:item.name});
          break;
        }
      }
    });
  }
},
  computed: {
    filter () {
    return this.caseSensitive
    ? (item, search, textKey) => item[textKey].indexOf(search) > -1
    : undefined
    },
  },
  mounted() {
     this.changeTree(this.roles);
  },


}
</script>


<style scoped>

</style>