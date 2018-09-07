var exec = require('cordova/exec');
var PushAPI = {}

PushAPI.initPushMethod = function (arg0, success, error) {
    exec(success, error, 'PushPlugin', 'initPushMethod', [arg0]);
};
PushAPI.receiveMessage = function(arg0, success, error) {
    exec(success, error, "PushPlugin", "receiveMessage", [arg0]);
};
PushAPI.getPushToken = function(arg0, success, error) {
    exec(success, error, "PushPlugin", "getPushToken", [arg0]);
};
module.exports = PushAPI;