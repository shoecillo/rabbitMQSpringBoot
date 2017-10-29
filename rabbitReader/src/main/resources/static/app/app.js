

var mainApp = angular.module("rabbitReceptor", ["ui.bootstrap"]);

mainApp.controller("receiverCtrl",["$scope","$timeout",function($scope,$timeout)
{
	
	var vm = $scope;
	vm.results = [];
	vm.sessionData = {};
	var sessionStart = new Date().getTime();
	vm.isAnimated=false;
	
	var timer;
	
	vm.sessionData.sessionStart = sessionStart;
	vm.sessionData.fmtSessionStart =  moment(sessionStart).format("YYYY-MM-DD-HH:mm:ss.SSS");
	vm.sessionData.numMsg = 0;
	vm.sessionData.backUp = [];
	vm.sessionData.maxMsg=10;
	
	
	var listener = function()
	{
		const eventSource = new EventSource('/stream.action');
		eventSource.onmessage = e => {
			const msg = JSON.parse(e.data);		
			
			if(vm.sessionData.maxMsg == vm.sessionData.numMsg )
			{
				if(vm.sessionData.backUp.length >0) 
				{
					vm.sessionData.backUp.concat(vm.results);
				}
				else
				{
					vm.sessionData.backUp=vm.results;
				}
				vm.results = [];
				vm.sessionData.numMsg = vm.results.length;
			}
			
			var date = new Date();
			var mills = date.getTime();
			msg.mills = mills;
			msg.timestamp = moment(mills).format("YYYY-MM-DD-HH:mm:ss.SSS");
			vm.results.push(msg);			
			vm.results = _.sortBy(vm.results,'mills');
			vm.results = vm.results.reverse();
			
			vm.sessionData.numMsg = vm.results.length;
			
			
			
			vm.isAnimated=true;
			timer = $timeout(stopAnim,5000);
			
			
			vm.$digest();
			
		};
		
		eventSource.onopen = e => console.log('open');
		eventSource.onerror = e => {
			if (e.readyState == EventSource.CLOSED) {
				console.log('close');
			}
			else {
				console.log(e);
			}
		};
	};
	
	var stopAnim = function()
	{
		vm.isAnimated=false;
		vm.$digest();
	};
	
	
	listener();
	
}]);