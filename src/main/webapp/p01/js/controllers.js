angular.module('app.controllers', [])

.service('urlService', [function(){
	this.baseURL = "/mc01/McService.svc/";
}])

.service('patientService', [function($odataresource){
	this.currentPatientID = 10;

	this.currentPatient;
}])

.service('orderService', [function(){
	this.currentOrder;
	
	this.isExistingOrder;
	this.isDoctorFixed;
	
	
}])
.controller('indexPageCtrl', function($scope, $state, $ionicActionSheet, orderService) {
	
	
	$scope.createOrder = function() {
		$scope.hideSheet = $ionicActionSheet.show({
		  buttons: [
			{ text: '指定医生' }
		  ],
		  titleText: '新建订单',
		  cancelText: '取消',
		  buttonClicked: function(index) {
			  orderService.isExistingOrder = false;
			  orderService.isDoctorFixed = (1===index);
			  $state.go('mc-sidemenu.createOrderPage');
		  }
		});
	}
	
  $scope.showImage=function(){
	  $scope.imageSrc  = 'http://localhost:28080/mc/img/k9n0gpcbRiFIUhLF7S8Q_zuhuai2.jpg';
      $scope.openModal();
  }

})
   
.controller('historyOrderPageCtrl', function($scope,$odataresource,urlService,orderService,patientService) {
    $scope.results = 
        $odataresource(urlService.baseURL  + "Orders")
        .odata()
		.filter("CTPatient/ID",patientService.currentPatientID)
		.filter("IsArchived", "Y")
        .query();

  $scope.getDetail=function(ObjectData){
	orderService.currentOrder=ObjectData;
  }
})
   
.controller('orderDetailPageCtrl', function($scope,$odataresource, $stateParams, urlService,orderService) {		
  $scope.currentOrder=orderService.currentOrder;
  page = {};
  page.title = '已下单';
  page.actionIcon = 'ion-android-chat';
  if(orderService.currentOrder && orderService.currentOrder.IsArchived === 'Y'){
	page.title = '历史订单';
	page.actionIcon = 'ion-locked';
  }
  $scope.page = page;
  $scope.getConvs=function(ObjectData){
	orderService.currentOrder=ObjectData;
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
    $scope.results = 
        $odataresource(urlService.baseURL  + "Orders")
        .odata()
		.filter("CTPatient/ID",patientService.currentPatientID)
		.filter("IsArchived", "N")
        .query();

  $scope.getDetail=function(ObjectData){
	orderService.currentOrder=ObjectData;
  }
})
   
.controller('100345Ctrl', function($scope) {

})
   
.controller('createOrderPageCtrl', function($scope, $state, $odataresource, $stateParams, urlService, orderService, patientService) {
	if(orderService.isExistingOrder){
		$scope.pageTitle = "编辑订单";
		$scope.buttonLabel = "提交";
		tempOrder = orderService.currentOrder;
	}else{
		$scope.pageTitle = "新建订单";
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
		myOrder.CTDoctor = {};
		myOrder.CTDoctor.ID = tempOrder.CTDoctor.ID;
		myOrder.Status = 'complete';
		
		myOrder.$save(
		    function(myOrder){
			orderService.currentOrder = myOrder;
			$state.go('mc-sidemenu.orderDetailPage');
			},function(myOrder){
			
			}
		
		);
	}
})
.controller('1003452Ctrl', function($scope) {

})
   
.controller('1003453Ctrl', function($scope) {

})
 