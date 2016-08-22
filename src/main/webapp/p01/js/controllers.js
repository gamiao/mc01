angular.module('app.controllers', [])

.service('urlService', [function(){
	this.baseURL = "/mc01/McService.svc/";
}])

.service('patientService', [function($odataresource){
	this.currentPatientID = 10;
	this.currentPatient;
}])

.service('doctorService', [function($odataresource){
	this.allDoctorList;
}])

.service('orderService', [function(){
	this.currentOrder;
	this.isExistingOrder;
	this.isDoctorFixed;
}])

.service('uploadService', [function(){
	this.imgUploadBase64URL = "/mc01/spring/uploadImgBase64/";
	this.param1;
	this.param2;
	this.param3;
	this.fileContent;
	this.fileName;
}])

.controller('indexPageCtrl', function($scope, $state, urlService, $odataresource, $ionicModal, $ionicActionSheet, orderService, doctorService) {
	
	$scope.createOrder = function() {
		$scope.hideSheet = $ionicActionSheet.show({
		  buttons: [
       { text: '<b>不指定医生</b>' },
       { text: '<b>指定医生</b>' }
		  ],
		  titleText: '新建咨询',
		  cancelText: '取消',
		  buttonClicked: function(index) {
			  orderService.isExistingOrder = false;
			  orderService.isDoctorFixed = (1===index);
			  $state.go('p-sm.createOrderPage');
		  }
		});
	}
	
  $scope.showImage=function(){
	  $scope.imageSrc  = 'http://localhost:28080/mc/img/k9n0gpcbRiFIUhLF7S8Q_zuhuai2.jpg';
      $scope.openModal();
  }
  
  
  $scope.selectables = [
    1, 2, 3
  ];

  $scope.longList  = [];
  for(var i=0;i<1000; i++){
    $scope.longList.push(i);
  }

  $scope.selectableNames =  [
    { name : "Mauro", role : "black hat"}, 
    { name : "Silvia", role : "pineye"},
    { name : "Merlino", role : "little canaglia"},
  ];

  $scope.someSetModel = 'Mauro';

  $scope.getOpt = function(option){
    return option.name + ":" + option.role;
  };
  
  $scope.shoutLoud = function(newValuea, oldValue){
    alert("changed from " + JSON.stringify(oldValue) + " to " + JSON.stringify(newValuea));
  };
  
  $scope.shoutReset = function(){
    alert("value was reset!");
  };
  

})
   
.controller('historyOrderPageCtrl', function($scope,$odataresource,urlService,orderService,patientService) {
	Order = $odataresource(urlService.baseURL  + 'Orders', 'ID');
    $scope.results = Order.odata()
		.filter("CTPatient/ID",patientService.currentPatientID)
		.filter("IsArchived", "Y")
        .query();

  $scope.getDetail=function(ObjectData){
	orderService.currentOrder=ObjectData;
  }
})
   
.controller('orderDetailPageCtrl', function($scope,$ionicModal,$ionicActionSheet, $state, $odataresource, $stateParams, urlService,orderService) {		
  var image = {};
  image.src1 = 'jiaohuai1.jpg';
  image.src2 = 'jiaohuai2.jpg';
  image.src3 = 'add_img.png';
  $scope.image = image;
  

  $scope.currentOrder=orderService.currentOrder;
  page = {};
  page.title = '进行中咨询';
  page.orderType = 'ongoing';
  if(orderService.currentOrder && orderService.currentOrder.IsArchived === 'Y'){
	page.title = '历史咨询';
	page.orderType = 'archived';
  } else if(orderService.currentOrder && orderService.currentOrder.Status === 'new'){
	page.title = '待接单';
	page.orderType = 'new';
  } else if(orderService.currentOrder && orderService.currentOrder.Status === 'unpaid'){
	page.title = '待付款';
	page.orderType = 'unpaid';
  }
  
  $scope.page = page;
  $scope.getConvs=function(ObjectData){
	orderService.currentOrder=ObjectData;
	$state.go('p-sm.orderConvsPage');
  }
  
    $ionicModal.fromTemplateUrl('templates/fullScreenImage.html', {
    scope: $scope,
    animation: 'slide-in-up'
  }).then(function(modal) {
    $scope.modal = modal;
	$scope.modal.scope.closeImgModal = function() {
		$scope.modal.hide();
	}
  $scope.openModal = function() {
    $scope.modal.show();
  };
  $scope.closeModal = function() {
    $scope.modal.hide();
  };
  // Cleanup the modal when we're done with it!
  $scope.$on('$destroy', function() {
    $scope.modal.remove();
  });
  // Execute action on hide modal
  $scope.$on('modal.hidden', function() {
    // Execute action
  });
  // Execute action on remove modal
  $scope.$on('modal.removed', function() {
    // Execute action
  });
  
  });
  
  $scope.openFullScreenImage = function(imgSrc){
	  $scope.modal.scope.imageSrc = imgSrc;
	  $scope.modal.show();
  }
  
  
  
  
  $scope.pay=function(order){
	actionSheetTiyle = "请确认";
	pickupButtonLabel = "确认付款";
	$scope.hideSheet = $ionicActionSheet.show({
      buttons: [
        { text: pickupButtonLabel }
      ],
      titleText: actionSheetTiyle,
      cancelText: '我再想想',
      buttonClicked: function(index) {
		  order.Status = 'ongoing';
		  order.$update(
				function(order){
					orderService.currentOrder=order;
					$scope.currentOrder=orderService.currentOrder;
					$scope.page.title = '进行中咨询';
					$scope.page.orderType='ongoing';
				},function(myOrderConv){
				}
			);
			return true;
      }
    });
  }
  
})
      
.controller('patientInfoPageCtrl', function($scope, uploadService, $odataresource, $stateParams, urlService,patientService) {
		patientService.currentPatient = $odataresource(urlService.baseURL  + "Patients("+ patientService.currentPatientID +")")
        .odata()
        .single();
    $scope.patient = patientService.currentPatient;
})
   
.controller('patientUpdatePageCtrl', function($scope,$state,$odataresource,uploadService, $stateParams, urlService,patientService) {
	test = $odataresource(urlService.baseURL  + "Patients("+ patientService.currentPatientID +")")
        .odata()
        .single();
    $scope.patient = patientService.currentPatient;
	
	$scope.updatePatient=function(){
	   patientService.currentPatient.$update();
  }
  
  	$scope.updateImage=function(){
	   uploadService.param1 = "Patient";
	   uploadService.param2 = patientService.currentPatientID;
	   uploadService.param3 = "Avatar";
	   $state.go('p-sm.imageUploadPage');
  }
})
   
   
.controller('imageUploadPageCtrl', function($rootScope,$ionicHistory, uploadService,Upload,$scope,$odataresource, $stateParams, urlService, $ionicActionSheet) {

		$scope.progressval = 0;

      $scope.browseFile=function(){
        document.getElementById('browseBtn').click();
      }

      angular.element(document.getElementById('browseBtn')).on('change',function(e){
        var file=e.target.files[0];
        angular.element(document.getElementById('browseBtn')).val('');
        var fileReader=new FileReader();
        fileReader.onload=function(event){
          $rootScope.$broadcast('event:file:selected',{fileData:event.target.result, fileName:file.name})
        }
        fileReader.readAsDataURL(file);
      });

    $rootScope.$on('event:file:selected',function(event,data){
        $scope.imgSrc = data.fileData;
		$scope.$apply();
		uploadService.fileData = data.fileData;
		uploadService.fileName = data.fileName;
    });

	$scope.uploadFile=function(){
		
        uploadUrl= uploadService.imgUploadBase64URL + uploadService.param1 + 
				"/" + uploadService.param2 + "/" + uploadService.param3;
		Upload.upload({
            url: uploadUrl,
            data: {fileData: uploadService.fileData, fileName: uploadService.fileName}
        }).then(function (resp) {
			$ionicHistory.goBack();
            console.log('Success ');
        }, function (resp) {
            console.log('Error status: ' + resp.status);
        }, function (evt) {
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
			$scope.progressval = progressPercentage;
            console.log('progress: ');
        });
    }
})
   
.controller('openOrderPageCtrl', function($scope,$odataresource,urlService,orderService,patientService) {
	Order = $odataresource(urlService.baseURL  + 'Orders', 'ID');
    $scope.results = Order.odata()
		.filter("CTPatient/ID",patientService.currentPatientID)
		.filter("IsArchived", "N")
        .query();

  $scope.getDetail=function(ObjectData){
	orderService.currentOrder=ObjectData;
  }
})
   
.controller('createOrderPageCtrl', function($scope,$ionicModal, $state, $odataresource, $stateParams, urlService, orderService, doctorService, patientService) {
	$scope.isDoctorFixed = orderService.isDoctorFixed;

	
	if(orderService.isExistingOrder){
		$scope.pageTitle = "编辑咨询";
		$scope.buttonLabel = "提交";
		tempOrder = orderService.currentOrder;
	}else{
		$scope.pageTitle = "新建咨询";
		$scope.buttonLabel = "创建";
		tempOrder = {};
		tempOrder.CTPatient = {};
		tempOrder.CTDoctor = {};
		tempOrder.CTDoctor.ID = 5;
		tempOrder.CTDetail = {};
	}
	$scope.currentOrder = tempOrder;
	
	$scope.createOrder =function(tempOrder){
	    Order = $odataresource(urlService.baseURL  + 'Orders', 'id');
		
		var myOrder = new Order();
		myOrder.CTPatient = {};
		myOrder.CTPatient.ID = patientService.currentPatientID;
		myOrder.CTDetail = {};
		myOrder.CTDetail.Description = tempOrder.CTDetail.Description;
		if($scope.isDoctorFixed){
			myOrder.CTDoctor = {};
			myOrder.CTDoctor.ID = tempOrder.CTDoctor.ID;
		}
		myOrder.Status = 'new';
		
		myOrder.$save(
		    function(myOrder){
			orderService.currentOrder = myOrder;
			$state.go('p-sm.orderDetailPage',null,{reload:true});
			},function(myOrder){
			
			}
		
		);
	}
	
	if($scope.isDoctorFixed){
	
	allDoctors = 
        $odataresource(urlService.baseURL  + "Doctors")
        .odata()
        .query();
	
  $ionicModal.fromTemplateUrl('templates/doctorSelectionPage.html', {
    scope: $scope,
    animation: 'slide-in-up'
  }).then(function(modal) {
    $scope.modal = modal;
	$scope.modal.scope.doctors = allDoctors;
	$scope.modal.scope.pageTitle = '请选择一位医生';
	$scope.modal.scope.setOption = function(doctor) {
		$scope.currentOrder.CTDoctor = doctor;
		$scope.modal.hide();
	}
	$scope.modal.scope.returnClicked = function() {
		$scope.modal.hide();
	}
  $scope.openModal = function() {
    $scope.modal.show();
  };
  $scope.closeModal = function() {
    $scope.modal.hide();
  };
  // Cleanup the modal when we're done with it!
  $scope.$on('$destroy', function() {
    $scope.modal.remove();
  });
  // Execute action on hide modal
  $scope.$on('modal.hidden', function() {
    // Execute action
  });
  // Execute action on remove modal
  $scope.$on('modal.removed', function() {
    // Execute action
  });
  
  });
  
    $scope.$on('$ionicView.enter', function() {
     // Code you want executed every time view is opened
     $scope.openModal();
  })
  
  
		
	}
  
})
   
.controller('createOrderConvPageCtrl', function($scope, $state, $odataresource, $stateParams, urlService, orderService) {
	$scope.currentOrder=orderService.currentOrder;
	tempOrderConv = {};
	$scope.currentOrderConv = tempOrderConv;
	
	$scope.createOrderConv =function(tempOrderConv){
	    OrderConv = $odataresource(urlService.baseURL  + 'Orders(' + orderService.currentOrder.ID + ')/OrderConvs', 'id');
		
		var myOrderConv = new OrderConv();
		myOrderConv.Type = 'TEXT';
		myOrderConv.Owner = 'P';
		myOrderConv.Description = tempOrderConv.Description;
		
		myOrderConv.$save(
		    function(myOrderConv){
				$state.go('p-sm.orderDetailPage');
			},function(myOrderConv){
			}
		);
	}
})

.controller('MainCtrl', ['$scope', function ($scope) {
  $scope.selectables = [
    1, 2, 3
  ];

  $scope.longList  = [];
  for(var i=0;i<1000; i++){
    $scope.longList.push(i);
  }

  $scope.selectableNames =  [
    { name : "Mauro", role : "black hat"}, 
    { name : "Silvia", role : "pineye"},
    { name : "Merlino", role : "little canaglia"},
  ];

  $scope.someSetModel = 'Mauro';

  $scope.getOpt = function(option){
    return option.name + ":" + option.role;
  };
  
  $scope.shoutLoud = function(newValuea, oldValue){
    alert("changed from " + JSON.stringify(oldValue) + " to " + JSON.stringify(newValuea));
  };
  
  $scope.shoutReset = function(){
    alert("value was reset!");
  };
  
}])



.controller('orderConvsPageCtrl', function($scope, $ionicModal, uploadService, $rootScope, $state, $stateParams, $ionicActionSheet,
    $ionicPopup, $ionicScrollDelegate, $timeout, $interval,orderService,urlService,$odataresource) {
		
	OrderConv = $odataresource(urlService.baseURL  + 'Orders(' + orderService.currentOrder.ID + ')/OrderConvs', 'id');
		
    orderConvs = OrderConv.odata().query();
		
	currentOrder = orderService.currentOrder;
	
  page = {};
  page.title = '咨询互动';
  page.enableNewMessage = true;
  page.newMessageHolder = '发消息给'+ currentOrder.CTDoctor.Name +'：';
  if(orderService.currentOrder && orderService.currentOrder.IsArchived === 'Y'){
	page.title = '互动历史';
	page.enableNewMessage = false;
	page.newMessageHolder = '已停止互动';
  } else if(orderService.currentOrder && orderService.currentOrder.Status === 'new'){
	page.enableNewMessage = false;
	page.newMessageHolder = '待病人付款后互动';
  }
    $scope.page = page;
	
	
    $scope.imageSrc = "jiaohuai1.jpg";
	$scope.currentOrder = currentOrder;

    // this could be on $rootScope rather than in $stateParams
	$scope.user = currentOrder.CTPatient;
	$scope.user._id = 'P-' + currentOrder.CTPatient.ID;

    $scope.toUser = currentOrder.CTDoctor;
	$scope.toUser._id = 'D-' + currentOrder.CTDoctor.ID;

    $scope.input = {
      message: localStorage['userMessage-' + $scope.toUser._id] || ''
    };

    var messageCheckTimer;

    var viewScroll = $ionicScrollDelegate.$getByHandle('userMessageScroll');
    var footerBar; // gets set in $ionicView.enter
    var scroller;
    var txtInput; // ^^^

    $scope.$on('$ionicView.enter', function() {
      console.log('UserMessages $ionicView.enter');

      getMessages();
      
      $timeout(function() {
        footerBar = document.body.querySelector('#userMessagesView .bar-footer');
        scroller = document.body.querySelector('#userMessagesView .scroll-content');
        txtInput = angular.element(footerBar.querySelector('textarea'));
      }, 0);

      messageCheckTimer = $interval(function() {
        // here you could check for new messages if your app doesn't use push notifications or user disabled them
      }, 20000);
    });

    $scope.$on('$ionicView.leave', function() {
      console.log('leaving UserMessages view, destroying interval');
      // Make sure that the interval is destroyed
      if (angular.isDefined(messageCheckTimer)) {
        $interval.cancel(messageCheckTimer);
        messageCheckTimer = undefined;
      }
    });

    $scope.$on('$ionicView.beforeLeave', function() {
      if (!$scope.input.message || $scope.input.message === '') {
        localStorage.removeItem('userMessage-' + $scope.toUser._id);
      }
    });

    function getMessages() {
      // the service is mock but you would probably pass the toUser's GUID here
        $scope.doneLoading = true;
        $scope.messages = orderConvs;

        $timeout(function() {
          viewScroll.scrollBottom();
        }, 0);
    }

    $scope.$watch('input.message', function(newValue, oldValue) {
      console.log('input.message $watch, newValue ' + newValue);
      if (!newValue) newValue = '';
      localStorage['userMessage-' + $scope.toUser._id] = newValue;
    });
	
	$scope.addImage = function() {
		uploadService.param1 = "OrderConv";
		uploadService.param2 = orderService.currentOrder.ID;
		uploadService.param3 = "P";
		$state.go('p-sm.imageUploadPage');
	}
	
	
  
  $ionicModal.fromTemplateUrl('templates/fullScreenImage.html', {
    scope: $scope,
    animation: 'slide-in-up'
  }).then(function(modal) {
    $scope.modal = modal;
	$scope.modal.scope.closeImgModal = function() {
		$scope.modal.hide();
	}
  $scope.openModal = function() {
    $scope.modal.show();
  };
  $scope.closeModal = function() {
    $scope.modal.hide();
  };
  // Cleanup the modal when we're done with it!
  $scope.$on('$destroy', function() {
    $scope.modal.remove();
  });
  // Execute action on hide modal
  $scope.$on('modal.hidden', function() {
    // Execute action
  });
  // Execute action on remove modal
  $scope.$on('modal.removed', function() {
    // Execute action
  });
  
  });
  
  $scope.openFullScreenImage = function(imgSrc){
	  $scope.modal.scope.imageSrc = imgSrc;
	  $scope.modal.show();
  }
	

    $scope.sendMessage = function(sendMessageForm) {

		var myOrderConv = new OrderConv();
		myOrderConv.Type = 'TEXT';
		myOrderConv.Owner = 'P';
		myOrderConv.Description = $scope.input.message;
		
		myOrderConv.$save(
		    function(myOrderConv){
			},function(myOrderConv){
			}
		);

      // if you do a web service call this will be needed as well as before the viewScroll calls
      // you can't see the effect of this in the browser it needs to be used on a real device
      // for some reason the one time blur event is not firing in the browser but does on devices
      keepKeyboardOpen();
      $scope.input.message = '';

      $scope.messages.push(myOrderConv);

      $timeout(function() {
        keepKeyboardOpen();
        viewScroll.scrollBottom(true);
      }, 0);

      $timeout(function() {
        //$scope.messages.push(MockService.getMockMessage());
        keepKeyboardOpen();
        viewScroll.scrollBottom(true);
      }, 2000);

      //});
    };
    
    // this keeps the keyboard open on a device only after sending a message, it is non obtrusive
    function keepKeyboardOpen() {
      console.log('keepKeyboardOpen');
      txtInput.one('blur', function() {
        console.log('textarea blur, focus back on it');
        txtInput[0].focus();
      });
    }

    $scope.onMessageHold = function(e, itemIndex, message) {
      console.log('onMessageHold');
      console.log('message: ' + JSON.stringify(message, null, 2));
      $ionicActionSheet.show({
        buttons: [{
          text: 'Copy Text'
        }, {
          text: 'Delete Message'
        }],
        buttonClicked: function(index) {
          switch (index) {
            case 0: // Copy Text
              //cordova.plugins.clipboard.copy(message.text);

              break;
            case 1: // Delete
              // no server side secrets here :~)
              $scope.messages.splice(itemIndex, 1);
              $timeout(function() {
                viewScroll.resize();
              }, 0);

              break;
          }
          
          return true;
        }
      });
    };

    // this prob seems weird here but I have reasons for this in my app, secret!
    $scope.viewProfile = function(msg) {
      if (msg.userId === $scope.user._id) {
        // go to your profile
      } else {
        // go to other users profile
      }
    };
    
    // I emit this event from the monospaced.elastic directive, read line 480
    $scope.$on('taResize', function(e, ta) {
      console.log('taResize');
      if (!ta) return;
      
      var taHeight = ta[0].offsetHeight;
      console.log('taHeight: ' + taHeight);
      
      if (!footerBar) return;
      
      var newFooterHeight = taHeight + 10;
      newFooterHeight = (newFooterHeight > 44) ? newFooterHeight : 44;
      
      footerBar.style.height = newFooterHeight + 'px';
      scroller.style.bottom = newFooterHeight + 'px'; 
    });

})

function onProfilePicError(ele) {
  this.ele.src = ''; // set a fallback
}

// configure moment relative time
moment.locale('en', {
  relativeTime: {
    future: "in %s",
    past: "%s ago",
    s: "%d sec",
    m: "a minute",
    mm: "%d minutes",
    h: "an hour",
    hh: "%d hours",
    d: "a day",
    dd: "%d days",
    M: "a month",
    MM: "%d months",
    y: "a year",
    yy: "%d years"
  }
})



moment.locale('cn', {
  relativeTime: {
    future: "%s以后",
    past: "%s前",
    s: "%d秒",
    m: "a分",
    mm: "%d分",
    h: "小时",
    hh: "%d小时",
    d: "天",
    dd: "%d天",
    M: "月",
    MM: "%d月",
    y: "年",
    yy: "%d年"
  }
})

 