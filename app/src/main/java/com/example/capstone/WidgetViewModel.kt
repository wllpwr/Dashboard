package com.example.capstone

import android.util.Base64
import androidx.lifecycle.ViewModel

class WidgetViewModel : ViewModel() {
    private val unencodedWidget1 = "<html>\n" +
            "  <head>\n" +
            "    <title>Time</title>\n" +
            "  </head>\n" +
            "  <body>\n" +
            "    <p>Current Time</p>\n" +
            "    <p id=\"timespot\">00:00</p>\n" +
            "\n" +
            "\n" +
            "    <script>\n" +
            "      \n" +
            "      window.onload = function() { setInterval(timeNow, 400); }\n" +
            "\n" +
            "      function timeNow(){\n" +
            "        var timeElement = document.getElementById(\"timespot\");\n" +
            "        let currentDate = new Date();\n" +
            "        var time = currentDate.getHours() + \":\" + currentDate.getMinutes() + \":\" + currentDate.getSeconds();\n" +
            "        timeElement.textContent = time\n" +
            "        console.log(time);\n" +
            "      }\n" +
            "    </script> \n" +
            "  </body>\n" +
            "</html>"
    private val unencodedWidget2 = "<html>\n" +
            "\n" +
            "<head>\n" +
            "  <title>Charting</title>\n" +
            "</head>\n" +
            "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.6.0/chart.js\"\n" +
            "\t\tintegrity=\"sha512-CWVDkca3f3uAWgDNVzW+W4XJbiC3CH84P2aWZXj+DqI6PNbTzXbl1dIzEHeNJpYSn4B6U8miSZb/hCws7FnUZA==\"\n" +
            "\t\tcrossorigin=\"anonymous\" referrerpolicy=\"no-referrer\"></script>\n" +
            "<body>\n" +
            "  <canvas id=\"chart\"></canvas>\n" +
            "\n" +
            "  <script>\n" +
            "    const ramctx = document.getElementById('chart').getContext(\"2d\");\n" +
            "\n" +
            "    const ramChart = new Chart(ramctx, {\n" +
            "      type: 'doughnut',\n" +
            "      data: {\n" +
            "        datasets: [{\n" +
            "          label: 'Testing',\n" +
            "          data: [43, 21, 59, 39],\n" +
            "          backgroundColor: [\n" +
            "            'rgba(138, 193, 72, 0.5)',\n" +
            "            'rgba(138, 193, 72, 0.0)',\n" +
            "          ],\n" +
            "          borderColor: [\n" +
            "            'rgba(138, 193, 72, 1)',\n" +
            "          ],\n" +
            "          borderWidth: 1\n" +
            "        }],\n" +
            "        labels: [\n" +
            "          '% used',\n" +
            "          '% free'\n" +
            "        ],\n" +
            "      },\n" +
            "\n" +
            "      options: {\n" +
            "        responsive: true,\n" +
            "        plugins: {\n" +
            "          tooltip: {\n" +
            "            enabled: true,\n" +
            "          },\n" +
            "        },\n" +
            "      }\n" +
            "    });\n" +
            "\n" +
            "  </script>\n" +
            "</body>\n" +
            "\n" +
            "</html>"

    val encodedWidget1: String = Base64.encodeToString(unencodedWidget1.toByteArray(), Base64.NO_PADDING)
    val encodedWidget2: String = Base64.encodeToString(unencodedWidget2.toByteArray(), Base64.NO_PADDING)
}