# hepburn

-- 20150722 16:39 --
暂时想法是图片加载用glide或者fresco，网络库用volley或者okhttp，图片流用support lib的recyclerview，遵循material design规范，所需的控件尽量只用support lib里面的。用java 8的lambda语法吧，与时俱进嘛。还想学RxAndroid。

-- 20150723 --
初步构建了网络层，实现了一个SwipeRefreshRecyclerView，并加入了load more的监听器，对RecyclerView使用还是比较生疏，没有达到理想的瀑布流效果。
