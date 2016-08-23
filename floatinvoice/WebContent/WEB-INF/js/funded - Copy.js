 angular.module('floatInvoiceListApp')
 .controller('FundedCtrl', ['$scope', '$http', '$routeParams', '$window', 'fiService',
 			function($scope, $http, $routeParams, $window, fiService){
			var acro = fiService.getAcronym();	
			$http.get('/floatinvoice/invoice/funded?acro='+acro)
			 .success(function(data){
    			$scope.invoices = data.list;
    			$scope.sortField = 'totPoolAmt';
   				 console.log(data);
    			});
		 	$scope.displayInvoices = function (poolRefId){
				$window.open('/floatinvoice/invoice/'+poolRefId, 'name', 'width=600,height=400');
			};
}]);

