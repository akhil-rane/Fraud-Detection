angular.module('finfloatInvoiceListApp')
  .controller('InvoiceFinViewCtrl', ['$scope', '$http','acroNameService', function ($scope, $http,acroNameService){
        var acro = acroNameService.getAcronym();  
        $http.get('/floatinvoice/invoice/view?acro='+acro).success(function(data){
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

        $scope.bidNow = function (refId){
          $scope.refId = refId;
          var data = JSON.stringify({
            refIds : [$scope.refId]           
          });
          $http.post('/floatinvoice/invoice/bid?acro='+acro, data).success($scope.refresh($scope.refId));
        };



      });
     }])
.controller('RightCtrlListView', ['$http', '$scope', '$window', '$timeout', '$mdSidenav', '$log', 'acroNameService', 
  function ($http, $scope, $window, $timeout, $mdSidenav, $log, acroNameService) {
    var smeAcro = acroNameService.getAcronym();  

  $scope.toggleRight = function(item, navID) {
        $scope.user = "";
        $scope.userForm.$setPristine();
        $scope.showIdListView = true;
        $scope.poolRefId = item.poolRefId;
        $scope.totPoolAmt = item.totPoolAmt;
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
      console.log("close" + $scope.showIdListView);
      $mdSidenav('right').close()
        .then(function () {
          $log.debug("close RIGHT is done");
        });
        $scope.userForm.$setPristine();
    };

    $scope.submitForm = function() {
      // check to make sure the form is completely valid
      $scope.isLoading = true;
      if ($scope.userForm.$valid) {
          $scope.user.acro = smeAcro;
          $scope.user.refId = $scope.poolRefId;
          $scope.user.totPoolAmt = $scope.totPoolAmt;
          $http({
              method:'POST',
              url:'/floatinvoice/invoice/bid?acro='+smeAcro,
              //$http.post('/floatinvoice/invoice/bid?acro='+acro, data)
              data:$scope.user,
              xhrFields: {
                  withCredentials: true
              },
              headers:{'Content-Type':'application/json'}
              }).then(function successCallback(response) {
                  $scope.showIdListView = false;
                  console.log(response);
                  $scope.refresh($scope.refId);
                   $scope.isLoading = false;
                }, function errorCallback(response) {
                  console.log(response);
                   $scope.isLoading = false;
            });
      }
    };

  }]);