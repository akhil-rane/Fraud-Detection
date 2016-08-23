 angular.module('floatInvoiceListApp')
 .controller('FundedCtrl', ['$scope', '$http', '$routeParams', '$window',
  'fiService', '$timeout', '$mdSidenav', '$log', function($scope, $http, $routeParams, 
    $window, fiService, $timeout, $mdSidenav, $log){
      var acro = fiService.getAcronym();  
    $http.get('/floatinvoice/invoice/funded?acro='+acro)
       .success(function(data){
          $scope.invoices = data.list;
          $scope.sortField = 'totPoolAmt';
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
           console.log(data);
          });

}])
  
.controller('FundedRightCtrl', ['$http', '$scope', '$window', '$timeout', '$mdSidenav', '$log', 'fiService', 
  function ($http, $scope, $window, $timeout, $mdSidenav, $log, fiService) {
    var smeAcro = fiService.getAcronym();  

  $scope.toggleRight = function(refId, navID) {
        console.log("toggleRight = " + refId);
        $scope.user = "";
        $scope.showId = true;
        $scope.refId = refId;
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

    $scope.submitForm = function() {
          console.log( " submitForm " + $scope.refId);
          $http({
              method:'POST',
              url:'/floatinvoice/invoice/acceptBid',
              data:JSON.stringify({"refId" : $scope.refId}),
              xhrFields: {
                  withCredentials: true
              },
              headers:{'Content-Type':'application/json'}
              }).then(function successCallback(response) {
                  $scope.showId = false;
                  console.log(response);
                  $scope.refresh($scope.refId);
                }, function errorCallback(response) {
                  console.log(response);
            });
      // check to make sure the form is completely valid
/*      if ($scope.userForm.$valid) {
          $scope.user.acro = smeAcro;
          console.log($scope.poolRefId);
          $http({
              method:'POST',
              url:'/floatinvoice/bank/saveAccount',
              data:$scope.user,
              xhrFields: {
                  withCredentials: true
              },
              headers:{'Content-Type':'application/json'}
              }).then(function successCallback(response) {
                  $scope.showId = false;
                  console.log(response);
                }, function errorCallback(response) {
                  console.log(response);
            });
      }*/
    };

  }]);