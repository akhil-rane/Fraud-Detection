 angular.module('floatInvoiceListApp')
 .controller('UnpaidCtrl', ['$scope', '$http', '$routeParams','fiService',
 			function($scope, $http, $routeParams, fiService){
 				var acro = fiService.getAcronym();	
				$http.get('/floatinvoice/invoice/view?acro='+acro)
		 		.success(function(data){
    			$scope.invoices = data.list;
    			$scope.sortField = 'amount';
   				$scope.refresh = function(refId){
   					console.log(refId);
					var index = -1;
					var invoiceList =  $scope.invoices;
					for (var i=0; i<invoiceList.length; i++){
						if( invoiceList[i].refId == refId ) {
								index = i;
								break;
						}
					}
					$scope.invoices.splice(index, 1);
				};
   				$scope.collectNow = function (refId){
					$scope.refId = refId;
					var data = JSON.stringify({
						refId : $scope.refId            
					});
					$http.post('/floatinvoice/invoice/credit', data).success($scope.refresh($scope.refId));
				};

    			});
		}]);