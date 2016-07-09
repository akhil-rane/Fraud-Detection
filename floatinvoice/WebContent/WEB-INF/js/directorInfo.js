
  angular.module("finfloatInvoiceListApp")
      .controller("DirectorCtrl", function($scope){
          $scope.productsDataSource = {
            type: "odata",
            serverFiltering: true,
            transport: {
                read: {
                    url: "//demos.telerik.com/kendo-ui/service/Northwind.svc/Products",
                }
            }
        };
    
        $scope.customersDataSource = {
            transport: {
                read: {
                    dataType: "jsonp",
                    url: "//demos.telerik.com/kendo-ui/service/Customers",
                }
            }
        };
    
        $scope.customOptions = {
            dataSource: $scope.customersDataSource,
            dataTextField: "ContactName",
            dataValueField: "CustomerID",
    
            headerTemplate: '<div class="dropdown-header k-widget k-header">' +
                '<span>Photo</span>' +
                '<span>Contact info</span>' +
                '</div>',
    
            // using {{angular}} templates:
            valueTemplate: '<span class="selected-value" style="background-image: url(\'//demos.telerik.com/kendo-ui/content/web/Customers/{{dataItem.CustomerID}}.jpg\')"></span><span>{{dataItem.ContactName}}</span>',
            
            template: '<span class="k-state-default" style="background-image: url(\'//demos.telerik.com/kendo-ui/content/web/Customers/{{dataItem.CustomerID}}.jpg\')"></span>' +
                      '<span class="k-state-default"><h3>{{dataItem.ContactName}}</h3><p>{{dataItem.CompanyName}}</p></span>',
        };    
      })
