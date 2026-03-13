(function () {
  // 拦截 wx.request
  if (typeof wx !== 'undefined' && typeof wx.request === 'function') {
    const _wxRequest = wx.request;
    wx.request = function (opts = {}) {
      const oldSuccess = opts.success;
      opts.success = function (res) {
        const d = res && res.data;
        if (d && d.code === 0 && d.msg === '超出配送范围') {
          wx.showModal({
            title: '提示',
            content: d.msg,
            showCancel: false
          });
          return;
        }
        oldSuccess && oldSuccess(res);
      };
      return _wxRequest.call(this, opts);
    };
  }
})();

require('./common/runtime.js')
require('./common/vendor.js')
require('./common/main.js')

