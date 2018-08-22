var exec = require('cordova/exec');
var PushAPI = {}

PushAPI.initPushMethod = function (arg0, success, error) {
    exec(success, error, 'PushPlugin', 'initPushMethod', [arg0]);
};
PushAPI.receivePushUrl = function(arg0, success, error) {
    exec(success, error, "PushPlugin", "receivePushUrl", [arg0]);
};
PushAPI.getPushToken = function(arg0, success, error) {
    exec(success, error, "PushPlugin", "getPushToken", [arg0]);
};
module.exports = PushAPI;