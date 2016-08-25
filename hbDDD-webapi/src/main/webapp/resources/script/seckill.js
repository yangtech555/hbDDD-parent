/**
 * Created by yanghongbo on 16/8/8.
 */
var wp="seckillDemo3";
var seckill = {
    webpath:function(){
        return
    },
    URL: {
        now: function () {
            return wp+"/seckill/time/now";
        },
        exposer: function (seckillId) {
            return wp+'/seckill/' + seckillId+'/exposer';
        },
        execution:function(seckillId,md5){
            return wp+'/seckill/'+seckillId+'/'+md5+'/execution';
        }},
    handleSeckill: function (seckillId, node) {
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    var  md5=exposer['md5'];
                    var killUrl=seckill.URL.execution(seckillId,md5);
                    console.log('killUrl:'+killUrl);
                    $('#killBtn').one('click',function(){
                        $(this).addClass('disabled');
                        $.post(killUrl,{},function(result){
                            if(result&&result['success']){
                                var killResult=result['data'];
                                var state=killResult['state'];
                                var stateInfo=killResult['stateInfo'];
                                node.html('<span class="label label-success">'+stateInfo+'</span>');
                            }
                        });
                    });
                    node.show();
                }
                else {
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    seckill.cdown(seckillId, now, start, end);
                }
            }
            else {
                console.log('result:' + result);
            }
        });
    },
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        }
        return false;
    },
    cdown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        if (nowTime < startTime) {
            seckillBox.html('秒杀未开始');
        }
        if (nowTime > endTime) {
            seckillBox.html('秒杀结束');
        }
        else if (nowTime < startTime) {
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                var format = event.strftime('秒杀倒计时:%D天 %H小时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown', function () {

            });
        }
        else {
            //  seckillBox.html('秒杀中');
            seckill.handleSeckill(seckillId,seckillBox);
        }
    },
    detail: {
        init: function (params) {
            wp=params["webpath"];
            var killPhone = $.cookie("killPhone");

            if (!seckill.validatePhone(killPhone)) {
                var killPhoneNodal = $('#killPhoneModal');
                killPhoneNodal.modal({
                    show: true,
                    backdrop: 'static',
                    keyboard: false
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {
                        $.cookie('killPhone', inputPhone, {expires: 7, path: wp+'/seckill'});
                        window.location.reload();
                    }
                    else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误</label>').show(300);
                    }
                });
            }
            var seckillId = params["seckillId"];
            var startTime = params["startTime"];
            var endTime = params["endTime"];
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result["success"]) {
                    var now = result["data"];
                    seckill.cdown(seckillId, now, startTime, endTime);
                }
                else {
                    console.log("result:" + result)
                }
            });

        }
    }
}/**
 * Created by yanghongbo on 16/8/25.
 */
