angular.module('app.controllers', [])

.controller("MyController",function($scope,$odataresource){
    $scope.results = 
        $odataresource("http://localhost:58080/mc01/McService.svc/Orders")
        .odata()
        .query();
})

.controller('page1Ctrl', function($scope) {

})
   
.controller('page2Ctrl', function($scope) {

})
   
.controller('page3Ctrl', function($scope) {

})
      
.controller('page4Ctrl', function($scope) {

})
   
.controller('100345Ctrl', function($scope) {

})
   
.controller('page7Ctrl', function($scope) {

})
   
.controller('page8Ctrl', function($scope) {

})
   
.controller('1003452Ctrl', function($scope) {

})
   
.controller('1003453Ctrl', function($scope) {

})
 