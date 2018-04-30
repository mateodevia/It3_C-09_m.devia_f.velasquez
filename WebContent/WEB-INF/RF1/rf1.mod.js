(function(ng) {
  var mod = angular.module('RF1Mod', ['ui.router']);

  mod.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
    
    $stateProvider.state('rf1', {
        views: {
          inputView: {
            templateUrl: "RF1/rf1.input.html",
            controller: "RF1Ctrl"
          }
        }
      })
      .state('rf1Response', {
        
    	  params: {
              nombre: null,
              tipo: null,
              tipoPersona: null,
              apellido: null,
              tipoDocumento: null,
              numDocumento: null
          },
    	  
    	  views: {
          inputView: {
            templateUrl: "RF1/rf1.input.html"
          },
          outputView: {
            templateUrl: "RF1/rf1.output.html",
            controller: "rf1ResponseCtrl"
          }
        }
      })
  }]);

})(window.angular);