 angular.module('finfloatInvoiceListApp')
 .controller('FinProfileTabsCtrl', ['$scope', '$location', 
      function ($scope, $location) {

      $scope.tabs = [
          { link : '#/t2/profiles/summary', label : 'Summary' },
          { link : '#/t2/profiles/directors', label : 'Directors' },
          { link : '#/t2/profiles/creditHist', label : 'Credit History' },
          { link : '#/t2/profiles/metrics', label : 'Metrics' },
          { link : '#/t2/profiles/cashflow', label : 'Cashflow' }
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