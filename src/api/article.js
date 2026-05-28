/*
 * @Author: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @Date: 2025-07-02 19:28:52
 * @LastEditors: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @LastEditTime: 2025-10-14 16:35:56
 * @FilePath: \blong\src\api\article.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import request from '@/utils/request.js'
import {useTokenStore} from '@/stores/token.js'
const tokenStore = useTokenStore();
//文章分类列表查询
export const articleCategoryListService = ()=>{
    //const tokenStore = useTokenStore();
    //在pinia中定义的响应式数据,都不需要.value
    //return request.get('/category',{headers:{'Authorization':tokenStore.token}})
    return request.get('/category')
}

//文章分类添加
export const articleCategoryAddService = (categoryData)=>{
    return request.post('/category',categoryData)
}

//文章分类修改
export const articleCategoryUpdateService = (categoryData)=>{
   return  request.put('/category',categoryData)
}

//文章分类删除
export const articleCategoryDeleteService = (id)=>{
    return request.delete('/category?id='+id)
}

//文章列表查询
export const articleListService = (params)=>{
    // console.log(params);
   return  request.get('/article',{params:params});
}

//文章添加
export const articleAddService = (articleData)=>{
    return request.post('/article',articleData);

}
//文章删除
export const articleDeleteService = (id)=>{
    return request.delete('/article?id='+id);
}
//文章修改
export const articleUpdateService = (articleData)=>{
    return request.put('/article',articleData);
}
//文章详情查询
export const articleAll=(params)=>{ 
    return request.get('/article/all',{params:params});
}
//文章信息
export const articlLists = (state) => {
//   console.log("传递的 state 参数：", state); 
  return request.get('/article/alls', {
    params: {state: state }});
};

export const articleStateService = (id, state) => { 
    return request.post('/article/state', null, { params: { id, state } });
}

export const articleReading=(id)=>{
   
    return request.get('/article/read?id='+id,tokenStore.token);
}

export const artAllRead=()=>{
    return request.get('/article/allReading');
}

export const artiTodeRead = () => {
  return request.get('/article/todayReading')
}

export const artiWeekReading=()=>{
    return request.get('/article/weekReading');
    }
export const artiAgnReading=()=>{
    return request.get('/article/avgReading');
    }

    //修改路径
    export const artiUpdaurl=()=>{
      return request.get('/article/updateurl');
    }

export const artiTimeReading = (startTime, endTime) => {
  return request.get('/article/totalReadingByTime', {
    params: {
      startTime, 
      endTime    
    }
  });
};
export const staticTableData = (id, readDate) => {
  return request.get('/statistic/getStatistic', {
    params: {
      articleId: id,
      readDate: readDate,
      token: tokenStore.token  
    }
  });
};

export const historyInstt=(history)=>{

  return request.post('/history/getHistory',history);
}

export const historyInsttAll = (userid) => {
  return request.post('/history/selectAll', null, {  
    params: { userid: userid } 
  });
};
export const historyInsttDelete=(userid)=>{

  return request.delete('/history?userid='+userid);
}

export const articlefiId=(id)=>{
  return request.get('/article/detail?id='+id)
}
