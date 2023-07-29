<template>
  <v-sheet color="#F5F6F7" width="546" rounded>
    <v-row class="mb-2">
      <v-icon small>mdi-vote-outline</v-icon>
      <span class="ml-2 body-2 grey--text text--darken-4"
        >{{vote.topic}}</span
      >
    </v-row>

    <div v-for="(item, voindex) in vote.options" :key="voindex" class="mt-6">
      <v-row>
        <span class="caption grey--text text--darken-4">{{ item.optionName }}</span>
        <v-spacer></v-spacer>
        <span class="caption grey--text text--darken-4 mr-2">{{
          item.count 
        }}票</span>
        <!-- <span class="caption grey--text text--darken-4">{{ item.point }}</span> -->
      </v-row>
      <v-row class="mt-4">
        <v-progress-linear
          :value="item.count"
          color="#1867C0"
          background-color="#E0E0E0"
        >
        </v-progress-linear>
      </v-row>
    </div>
  </v-sheet>
</template>
<script>


export default {
  props: {
    vote: {
      type: [String, Object],
      default: "",
    },
    
  },
  data() {
    return {
      
    };
  },

  methods: {
    getRandom(min, max) { //根据最小值和最大值产生一个随机数
      return Math.floor(Math.random() * (max - min) + min);
    }
  },
};
</script>