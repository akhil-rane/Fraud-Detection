 angular.module('floatInvoiceListApp')
 .controller('UploadCtrl', ['$scope', '$http', '$routeParams', 'fiService',
 			function($scope, $http, $routeParams, fiService){
				$scope.acro="/floatinvoice/invoice/upload?acro="+fiService.getAcronym();
}]);