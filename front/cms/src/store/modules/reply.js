const state = {
    content: {
        //回复内容
        replyText: '',
        //上传文件 
        upFiles: [],
    }
};

const mutations = {

    //添加/更新content内容
    update_content (state,antherText,antherFiles) {
        state.content.replyText = antherText;
        state.content.upFiles = antherFiles;
    },

    //清空value值
    clear_content(state){
        Object.keys(state.content).forEach((key) => (state.content[key] = ''))
    }

};


const actions = {
    //action -更新mutations
    UpdateContent ({commit},antherText,antherFiles) {
        commit("update_content", antherText,antherFiles)
    },

    //action -清空mutations
    ClearContent ({commit}) {
        commit("clear_content")
    }
};
const getters = {
    //返回content 
    getContent(state){
        return state.content;
    }
};

//把state, mutations等暴露出去。
export default {
    //需要命名空间
    namespaced: true,
    state,
    mutations,
    actions,
    getters
}