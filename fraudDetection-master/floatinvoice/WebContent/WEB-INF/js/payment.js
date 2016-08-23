 angular.module('floatInvoiceListApp')
 .controller('PaymentCtrl', ['$scope', '$http', '$routeParams','fiService',
 			function($scope, $http, $routeParams, fiService){
 				var acro = fiService.getAcronym();	
				$http.get('/floatinvoice/bank/viewActiveLoans?acro='+acro)
		 		.success(function(data){
    			$scope.loans = data.list;
    			$scope.sortField = 'loanId';
   				$scope.refresh = function(paymentDueDt){
					var loansList =  $scope.loans;
					for (var i=0; i<loansList.length; i++){
						var index = -1;
						for( var j=0; j<loansList[i].emis.length; j++){
							if( loansList[i].emis[j].paymentDueDt == paymentDueDt ) {
									index = j;
									break;
							}
						}
						if (index >= 0){
							$scope.loans[i].emis.splice(index, 1);
							break;
						}
					}					
				};
    			});
		}])
 .controller('EMIPaymentCtrl', ['$http', '$scope', '$window', '$timeout', '$mdSidenav', '$log', 'fiService', 
  function ($http, $scope, $window, $timeout, $mdSidenav, $log, fiService) {
    var smeAcro = fiService.getAcronym();  

  	$scope.toggleRight = function(refId, navID) {
        $scope.user = "";
        $mdSidenav(navID)
          .toggle()
          .then(function () {
            $log.debug("toggle " + navID + " is done");
          });
    }   

	$scope.isOpenRight = function(){
	      return $mdSidenav('right').isOpen();
	};

    /**
     * Supplies a function that will continue to operate until the
     * time is up.
     */
  	function debounce(func, wait, context) {
      var timer;
      return function debounced() {
        var context = $scope,
            args = Array.prototype.slice.call(arguments);
        $timeout.cancel(timer);
        timer = $timeout(function() {
          timer = undefined;
          func.apply(context, args);
        }, wait || 10);
      };
    }

    /**
     * Build handler to open/close a SideNav; when animation finishes
     * report completion in console
     */
    function buildDelayedToggler(navID) {
      return debounce(function() {
        $mdSidenav(navID)
          .toggle()
          .then(function () {
            $log.debug("toggle " + navID + " is done");
          });
      }, 200);
    }


    $scope.close = function () {
      console.log("close" + $scope.showId);
      $mdSidenav('right').close()
        .then(function () {
          $log.debug("close RIGHT is done");
        });
        //$scope.userForm.$setPristine();
    };

    $scope.submitForm = function(emi, financierAcronym, loanRefId) {
    	  $scope.loanRefId = loanRefId;
    	  $scope.emiAmount = emi.amt;
    	  $scope.paymentDueDt = emi.paymentDueDt;
          $http({
              method:'POST',
              url:'/floatinvoice/bank/payLoan',
              data:JSON.stringify({
              	"loanRefId" : $scope.loanRefId,
              	"amt" : $scope.emiAmount,
              	"smeAcro" : smeAcro,
              	"financier" : financierAcronym,
              	"paymentDueDt" : $scope.paymentDueDt,
              	"paidDt": "2016-03-24",
              	"fees":emi.fees
              }),
              xhrFields: {
                  withCredentials: true
              },
              headers:{'Content-Type':'application/json'}
              }).then(function successCallback(response) {
                  $scope.refresh($scope.paymentDueDt);
                }, function errorCallback(response) {
                  console.log(response);
            });

    };

  }]);