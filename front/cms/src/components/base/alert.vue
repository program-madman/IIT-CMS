<template>
   <v-dialog width="546" v-model="dialog">
      <v-card>
        <v-card-title>
          <span>提示</span>
        </v-card-title>
        <v-card-text>{{content}}</v-card-text>
        
          <div style="margin-top: 24px; height: 60px">
            <v-btn
              min-width="105px"
              height="36px"
              depressed
              dark
              color="blue"
              @click="onConfirm"
            >
              确认
            </v-btn>
          </div>
       
      </v-card>
    </v-dialog>
</template>

<script>
export default {
  props: {
    value: {
      type: String,
      default: "",
    },
   
  },
  data() {
    return {
      dialog:false,
      content:this.value,
    };
  },

  methods: {
    confirm(content){
      this.dialog = true;
      return new Promise((resolve, reject) => {
        this.content=content;
        this.onConfirm = (e) => { 
          this.dialog = false;
          resolve(e);
        }
      })
    },
    onConfirm() {
     
    },
  },
};
</script>