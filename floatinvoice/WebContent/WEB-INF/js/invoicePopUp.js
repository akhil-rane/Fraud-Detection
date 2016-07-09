angular.module('floatInvoiceListApp')
 .controller('InvoicePopUpCtrl', ['$scope', '$http', '$routeParams', '$location',
 			function($scope, $http, $routeParams, $location, $modalInstance, mydata){
			
			console.log($scope.poolRefId);
/*			var ModalInstanceCtrl = function($scope, $modalInstance, mydata) {
  $scope.mydata = mydata;

  $modalInstance.setMyData = function(theData) {
    $scope.mydata = theData;
  };

  $scope.ok = function() {
    $modalInstance.close('close');
  };
  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };
};*/

}]);