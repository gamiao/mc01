angular.module('app.controllers', [])

.service('urlService', [function(){
	this.baseURL = "/mc01/McService.svc/";
}])

.service('orderService', [function(){
	this.currentOrder;
}])
   
.controller('historyOrderPageCtrl', function($scope,$odataresource, $stateParams, urlService ,orderService) {	
    $scope.results = 
        $odataresource(urlService.baseURL  + "Orders")
        .odata()
        .query();

  $scope.getDetail=function(ObjectData){
	orderService.currentOrder=ObjectData;
  }
})
   
.controller('historyDetailPageCtrl', function($scope,$odataresource, $stateParams, urlService ,orderService) {		
  $scope.currentOrder=orderService.currentOrder;
  $scope.getConvs=function(ObjectData){
	orderService.currentOrder=ObjectData;
  }
})
   
.controller('historyConvsPageCtrl', function($scope,$odataresource, $stateParams, urlService ,orderService) {	
	if(orderService.currentOrder !== null && orderService.currentOrder.ID !== null){
    $scope.orderConvs = 
        $odataresource(urlService.baseURL  + "Orders("+ orderService.currentOrder.ID+")/OrderConvs")
        .odata()
        .query();
	}
	$scope.currentOrder=orderService.currentOrder;

})

.controller('indexPageCtrl', function($scope) {

})
   
.controller('page2Ctrl', function($scope) {

})
      
.controller('page4Ctrl', function($scope) {

})
   
.controller('100345Ctrl', function($scope) {

})
   
.controller('page7Ctrl', function($scope,$odataresource, $stateParams, orderService) {
})
   
.controller('page8Ctrl', function($scope) {

})
   
.controller('1003452Ctrl', function($scope) {

})
   
.controller('1003453Ctrl', function($scope) {

})
 