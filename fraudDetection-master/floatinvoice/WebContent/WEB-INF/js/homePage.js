 angular.module('floatInvoiceListApp')
 .controller('JustifiedTabsCtrl', ['$scope', '$location', 
      function ($scope, $location) {
      $scope.tabs = [
          { link : '#/s1/upload', label : 'Upload' },
          { link : '#/s1/unpaid', label : 'Unpaid' },
          { link : '#/s1/pending', label : 'Pending' },
          { link : '#/s1/funded', label : 'Bids' },
          { link : '#/s1/paid', label : 'Paid' },
          { link : '#/s1/delinquent', label : 'Delinquent' }
        ];
      var index = -1;
      var tabList =  $scope.tabs;
      for (var i=0; i<tabList.length; i++){
        if( tabList[i].link == '#'+$location.url() ) {
            index = i;
            break;
        }
      }
      $scope.selectedTab = $scope.tabs[index];
      $scope.setSelectedTab = function(tab, alignment) {
        $scope.selectedTab = tab;
      }
      $scope.tabClass = function(tab, alignment) {
        if ($scope.selectedTab == tab) {
          return "active";
        } else {
          return "";
        }
      }
    }]);