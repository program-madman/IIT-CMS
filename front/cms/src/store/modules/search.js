//search.js
// export const store = {
//     msgFilter:[],
//     addToFilter(dept,sDate,eDate){
//
//         const obj = {department: dept, startDate:sDate, endDate:eDate,search:'',done: true}
//         this.msgFilter.push(obj)
//         if(this.msgFilter.length>10)
//         {
//             this.msgFilter.remove(0);
//         }
//     },
//     addSearch(search){
//         this.msgFilter.filter(function (item){
//             if(item.done){
//                 item.search = search;
//             }
//         });
//     },
//     getLength(){
//         return this.msgFilter.length;
//     },
//     getFilter() {
//         for (let item of this.msgFilter) {
//             if (item.done) {
//                 if(item.search !== '')
//                     item.done = false;
//                 return item;
//             }else
//                 return {department: '', startDate:(new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10), endDate:(new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),search:'',done: false};
//         }
//     },
//     setFilterDone(index){
//         this.msgFilter[index].done = true
//     }
// }

export const store = {
    msgFilter:{
        articleType:0,
        department:'',
        startDate:'',
        endDate:'',
        search:'',


    }
}