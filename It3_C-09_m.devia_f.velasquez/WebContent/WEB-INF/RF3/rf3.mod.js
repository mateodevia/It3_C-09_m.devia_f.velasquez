(function (ng) {
    var mod = angular.module('RF3Mod', ['ui.router']);

    mod.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

            $stateProvider.state('rf3', {
                views: {
                    inputView: {
                        templateUrl: "RF3/rf3.input.html",
                        controller: "RF3Ctrl"
                    }
                }
            }).state('rf3Response', {

                params: {
                    carnetUniandes: null,
                    nombre: null,
                    apellido: null,
                    tipoCliente: null,
                    tipoDocumento: null,
                    numDocumento: null
                },

                views: {
                    inputView: {
                        templateUrl: "RF3/rf3.input.html"
                    },
                    outputView: {
                        templateUrl: "RF3/rf3.output.html",
                        controller: "responseRF3Ctrl"
                    }
                }
            });
        }]);

})(window.angular);