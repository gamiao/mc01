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
   
.controller('orderDetailPageCtrl', function($scope,$ionicActionSheet, $state, $odataresource, $stateParams, urlService,orderService) {		
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
   
.controller('orderConvsPageCtrl', function($scope,$odataresource, $stateParams, urlService,orderService) {
	if(orderService.currentOrder !== null && orderService.currentOrder.ID !== null){
    $scope.orderConvs = 
        $odataresource(urlService.baseURL  + "Orders("+ orderService.currentOrder.ID+")/OrderConvs")
        .odata()
        .query();
	}
	$scope.currentOrder=orderService.currentOrder;

})
      
.controller('patientInfoPageCtrl', function($scope,$odataresource, $stateParams, urlService,patientService) {
		patientService.currentPatient = $odataresource(urlService.baseURL  + "Patients("+ patientService.currentPatientID +")")
        .odata()
        .single();
    $scope.patient = patientService.currentPatient;
})
   
.controller('patientUpdatePageCtrl', function($scope,$odataresource, $stateParams, urlService,patientService) {
	test = $odataresource(urlService.baseURL  + "Patients("+ patientService.currentPatientID +")")
        .odata()
        .single();
    $scope.patient = patientService.currentPatient;
	
	$scope.updatePatient=function(){
	   patientService.currentPatient.$update();
  }
})
   
.controller('imageUploadPageCtrl', function($scope,$odataresource, $stateParams, urlService, $ionicActionSheet) {

 
  $scope.addMedia = function() {
    $scope.hideSheet = $ionicActionSheet.show({
      buttons: [
        { text: 'Take photo' },
        { text: 'Photo from library' }
      ],
      titleText: 'Add images',
      cancelText: 'Cancel',
      buttonClicked: function(index) {
		  $scope.takePic();
      }
    });
  }
 
  $scope.takePic = function() {
        var options =   {
            quality: 50,
            destinationType: Camera.DestinationType.FILE_URI,
            sourceType: 1,      // 0:Photo Library, 1=Camera, 2=Saved Photo Album
            encodingType: 0     // 0=JPG 1=PNG
        }
        navigator.camera.getPicture(onSuccess,onFail,options);
    }
    var onSuccess = function(FILE_URI) {
        console.log(FILE_URI);
        $scope.picData = FILE_URI;
        $scope.$apply();
    };
    var onFail = function(e) {
        console.log("On fail " + e);
    }
    $scope.send = function() {   
        var myImg = $scope.picData;
        var options = new FileUploadOptions();
        options.fileKey="post";
        options.chunkedMode = false;
        var params = {};
        params.user_token = localStorage.getItem('auth_token');
        params.user_email = localStorage.getItem('email');
        options.params = params;
        var ft = new FileTransfer();
        ft.upload(myImg, encodeURI("https://example.com/posts/"), onUploadSuccess, onUploadFail, options);
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
 