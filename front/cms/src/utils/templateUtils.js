
export const transformationAuthority = (authorityList, selectChild) => {

  let childrenArr = [];
  if (authorityList !== null && typeof authorityList !== "undefined") {
    authorityList.forEach(v => {
      if (v != null && typeof v !== "undefined" && JSON.stringify(v) !== "null") {
        v.name = v.menuName;
        v.locked = !v.isEnable;
        if(v.isCheck) {
          childrenArr.push(v)
        }
        if (v.children != null && typeof v.children !== "undefined") {
          v.children.forEach(c => {
            c.name = c.menuName;
            c.locked = !c.isEnable
            if (selectChild != null && typeof selectChild !== "undefined") {
              //清理一下selectchild
              console.log("select authority dept  ====>" + JSON.stringify(selectChild))
              selectChild = selectChild.filter(v => v !== null && typeof v !== "undefined" && JSON.stringify(v) !== "null")
              let result = selectChild.filter((element) => element.id === c.id)
              console.log("result select child  ====>" + JSON.stringify(result))
              if (typeof result !== "undefined") {
                c.isCheck = result.length > 0;
              }
            }
          })
          childrenArr = childrenArr.concat(v.children)
        }
      }
    });
    return childrenArr;
  }

}