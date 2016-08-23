 angular.module('floatInvoiceListApp')
 .controller('ProfileTabsCtrl', ['$scope', '$location', 
      function ($scope, $location) {
      $scope.tabs = [
          { link : '#/s4/dashbd', label : 'Dashboard' },
          { link : '#/s4/directorInfo', label : 'Directors' },
          { link : '#/s4/financing', label : 'Metrics' },
          { link : '#/s4/compInfo', label : 'Cash Flows' },
          
          { link : '#/s4/trends', label : 'Industry Trends' }
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