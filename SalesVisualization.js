google.charts.load('current', {packages: ['corechart', 'bar']});
// google.charts.setOnLoadCallback(drawStacked);

//Bind click event to make an ajax call to post request of DataVisualization. This will return
// a json object with top 3 review for each city;
$("#btnGetChartData").click(function () {
     $("#btnGetChartData").hide();
    $.ajax({
        url: "SalesReport",
        type: "POST",
        data: "{}",
        success: function (msg) {
            createDataTable(msg)            
        },
        error: function(){
            console.log("error occurred while making ajax call;")
        }
    });    
});


//This method will parse json data and build datatable required by google api to plot the bar chart.
function createDataTable(jsonData) {
    var parsedData = $.parseJSON(jsonData);
    var data = new Array();

    data.push(["Product Name", "Total Sales"]);
    //Create an array of product name and an array of zipcodes
    for(var i=0; i<parsedData.length; i++) {
        var productName = parsedData[i]["productName"];
        var totalSales = parsedData[i]["totalSales"];
        data.push([productName, totalSales])
     }
     
     drawChart(data);
}

//Plot the chart using 2d array and product names as subtitles;
function drawChart(data) {


    //Invoke google's built in method to get data table object required by google.
     var chartData = google.visualization.arrayToDataTable(data);
    var height = data.length * 50;
    var options = {
        title: "Product Sales Chart",
        width: 1000,
        height: height,
        bars: 'horizontal',
        bar: {groupWidth: "95%"},
        legend: { position: "none" },
        axes: {
            x: {
              0: { side: 'top', label: 'Total Sales'} // Top x-axis.
            }
          }
        };

    var chart = new google.visualization.BarChart(document.getElementById('chart_div'));
    chart.draw(chartData, options);
}




