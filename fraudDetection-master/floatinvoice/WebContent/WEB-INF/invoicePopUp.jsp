<html ng-app="floatInvoicePopUpView">
<script type="text/javascript">
      var floatInvoicePopUpView = angular.module('floatInvoicePopUpView',[]);
      floatInvoicePopUpView.controller('InvoicePopUpCtrl', 'lstMsgSvc', function ($scope, $http,
        lstMsgSvc){
        $scope.invoices = lstMsgSvc.getLstMsg();
      });
      floatInvoicePopUpView.service('lstMsgSvc', function(){
      this.getLstMsg = function(){
        return "${lstMsg}";
      };
    });


</script>
 <body>
 <div class="container">
<br/>

    <div class="row">  
      <div class="col-sm-7">
          <img src = "img/logo.jpg" height=70/>                 
      </div>

<!--       <div class="col-sm-5">
          <span style="float:right">
          <label> Welcome ${acronym}</label>
            <a href="/floatinvoice/logout" class="btn btn-primary" title="Logout" data-toggle="tooltip" >
            <span class="glyphicon glyphicon-log-out"></span></a>
          </span>
          
      </div> -->
    </div>
  <table ng-controller="InvoicePopUpCtrl" class="table table-striped">
    <tr>
      <th><a href="" ng-click="sortField = 'smeCtpy'">Buyer Name</td>
      <th><a href="" ng-click="sortField = 'amount'"> Amount</td>
      <th><a href="" ng-click="sortField = 'invoiceNo'">Invoice #</th>
      <th><a href="" ng-click="sortField = 'startDt'">Invoice Date</th>
      <th><a href="" ng-click="sortField = 'endDt'">Due Date</th>
      <th/>
    </tr>
    <tr>
         <th>
            <input type="text" ng-model="search.smeCtpy" placeholder="search for buyer" class="input-sm form-control"/>
        </th>
        <th>
            <input type="text" ng-model="search.amount" placeholder="search for amount" class="input-sm form-control"/>
        </th>
        <th>
            <input type="text" ng-model="search.invoiceNo" placeholder="search for invoice number" class="input-sm form-control" />
        </th>
        
         <th>
            <input type="text" ng-model="search.startDt" placeholder="search for invoice date" class="input-sm form-control" />
        </th>
         <th>
            <input type="text" ng-model="search.endDt" placeholder="search for due date" class="input-sm form-control" />
        </th>

      
         <th/>
    </tr>
    <tr dir-paginate="item in invoices | filter:search | orderBy:sortField| itemsPerPage: 10">
      <td>{{item.smeCtpy}}</td>
      <td>&#x20B9; {{item.amount}}</td>
      <td>{{item.invoiceNo}}</td>
      <td>{{item.startDt}}</td>
      <td>{{item.endDt}}</td>

    </tr>
  </table>
  <div align="center">
    <dir-pagination-controls/>
  </div>
</div>
</body>
</html>