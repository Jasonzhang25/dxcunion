(function () { 
 return angular.module('unionWebApp')
.constant('auth.config', {"Admin":["user","user.add","user.list","user.edit","user.uploademployee","ticket","ticket.fetch"],"Operator":["ticket","ticket.fetch"]});

})();
