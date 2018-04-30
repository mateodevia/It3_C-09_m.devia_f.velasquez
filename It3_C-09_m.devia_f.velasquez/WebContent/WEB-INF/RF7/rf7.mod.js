(function (ng) {
    var mod = angular.module('RF7Mod', ['ui.router']);

    mod.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

            $stateProvider.state('rf7', {
                views: {
                    inputView: {
                        templateUrl: "RF7/rf7.input.html",
                        controller: "RF7Ctrl"
                    }
                }
            }).state('rf7Response', {

                params: {
                	evento: null,
                	servicios: null,
                	fechaInicio: null,
                	fechaFin: null,
                	tipoContrato: null,
                	numPersonasPorReserva: null,
                	clienteId: null,
                	tipo:null,
                	necesitadas: null,
                	reservas:[]
                },

                views: {
                    inputView: {
                        templateUrl: "RF7/rf7.input.html"
                    },
                    outputView: {
                        templateUrl: "RF7/rf7.output.html",
                        controller: "responseRF7Ctrl"
                    }
                }
            });
        }]);

})(window.angular);