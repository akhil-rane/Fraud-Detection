
angular.module('finfloatInvoiceListApp')
/*.config(function($mdDateLocaleProvider) {


     $mdDateLocaleProvider.formatDate = function(date) {
      console.log(date);
       return moment(date).format('YYYY-MM-DD');
    };
})*/
  .controller('FinApprovalViewCtrl', ['$scope', '$http','acroNameService', function ($scope, $http,acroNameService){
        var acro = acroNameService.getAcronym();  
        $http.get('/floatinvoice/invoice/viewBids?acro='+acro).success(function(data){
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

        


      });
     }])

.controller('RightCtrl', ['$http', '$scope', '$window', '$timeout', '$mdSidenav', '$log', 'acroNameService', 
  function ($http, $scope, $window, $timeout, $mdSidenav, $log, acroNameService) {
  var financierAcro = acroNameService.getAcronym();  

      $scope.myDate = new Date();

      $scope.minDate = new Date(
        $scope.myDate.getFullYear(),
        $scope.myDate.getMonth(),
        $scope.myDate.getDate());
      
     // $scope.minCloseDate = $scope.minDate; 

      $scope.maxDate = new Date(
        $scope.minDate.getFullYear(),
        $scope.minDate.getMonth() + 4,
        $scope.minDate.getDate()); 

  $scope.toggleRight = function(item, navID) {
        $scope.user = "";
        $scope.userForm.$setPristine();
        $scope.loanAmtOffer = item.loanAmtOffer;
        $scope.showId = true;
        $scope.poolRefId = item.poolRefId;
        $scope.smeAcro = item.sme;
        console.log("poolRefId: " + item.poolRefId + " navID: "+ navID + ", " + $scope.showId);
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
        $scope.userForm.$setPristine();
    };

    $scope.submitForm = function() {
      // check to make sure the form is completely valid
      if ($scope.userForm.$valid) {
          $scope.user.financierAcro = financierAcro;
          $scope.user.smeAcro = $scope.smeAcro;
          $scope.user.poolRefId = $scope.poolRefId;
          $scope.user.loanAmt = $scope.loanAmtOffer;
          console.log($scope.user);
          $http({
              method:'POST',
              url:'/floatinvoice/bank/approveLoan',
              //$http.post('/floatinvoice/invoice/bid?acro='+acro, data)
              data:$scope.user,
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
      }
    };

  }])  ;
