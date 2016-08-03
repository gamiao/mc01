angular.module('app.controllers', [])

.service('urlService', [function(){
	this.baseURL = "/mc01/McService.svc/";
}])

.service('patientService', [function($odataresource){
	this.currentPatientID = 2;

	this.currentPatient;

}])

.service('orderService', [function(){
	this.currentOrder;
}])
.controller('indexPageCtrl', function($scope) {

})
   
.controller('historyOrderPageCtrl', function($scope,$odataresource,urlService,orderService) {
    $scope.results = 
        $odataresource(urlService.baseURL  + "Orders")
        .odata()
        .query();

  $scope.getDetail=function(ObjectData){
	orderService.currentOrder=ObjectData;
  }
})
   
.controller('historyDetailPageCtrl', function($scope,$odataresource, $stateParams, urlService,orderService) {		
  $scope.currentOrder=orderService.currentOrder;
  $scope.getConvs=function(ObjectData){
	orderService.currentOrder=ObjectData;
  }
})
   
.controller('historyConvsPageCtrl', function($scope,$odataresource, $stateParams, urlService,orderService) {
	if(orderService.currentOrder !== null && orderService.currentOrder.ID !== null){
    $scope.orderConvs = 
        $odataresource(urlService.baseURL  + "Orders("+ orderService.currentOrder.ID+")/OrderConvs")
        .odata()
        .query();
	}
	$scope.currentOrder=orderService.currentOrder;

})
      
.controller('patientInfoPageCtrl', function($scope,$odataresource, $stateParams, urlService,patientService) {
		patientService.currentPatient = $odataresource(urlService.baseURL  + "Patients(2)")
        .odata()
        .single();
    $scope.patient = patientService.currentPatient;
})
   
.controller('patientUpdatePageCtrl', function($scope,$odataresource, $stateParams, urlService,patientService) {
	test = $odataresource(urlService.baseURL  + "Patients(2)")
        .odata()
        .single();
	test.$update();
    $scope.patient = patientService.currentPatient;
	
	$scope.updatePatient=function(){
	   patientService.currentPatient.$update();
  }
})
   
   
.controller('page2Ctrl', function($scope) {

})
   
.controller('100345Ctrl', function($scope) {

})
   
.controller('page7Ctrl', function($scope, $odataresource, $stateParams, orderService) {
})
.controller('1003452Ctrl', function($scope) {

})
   
.controller('1003453Ctrl', function($scope) {

})
 