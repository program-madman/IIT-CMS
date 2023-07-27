<template>
  <div>
    <div class="row">
      <div class="col-xs-12">
        <div class="chart">
          <div class="no-chart-data">
            <h2 class="title">No data for current period</h2>
            <div class="y-axis"></div>
            <div class="x-axis"></div>
            <div class="bars">
              <div class="overlay"></div>
              <div class="bar bar-0"></div>
              <div class="bar bar-1"></div>
              <div class="bar bar-2"></div>
              <div class="bar bar-3"></div>
              <div class="bar bar-4"></div>
              <div class="bar bar-5"></div>
              <div class="bar bar-6"></div>
              <div class="bar bar-7"></div>
              <div class="bar bar-8"></div>
              <div class="bar bar-9"></div>
              <div class="bar bar-10"></div>
              <div class="bar bar-11"></div>
              <div class="bar bar-12"></div>
              <div class="bar bar-13"></div>
              <div class="bar bar-14"></div>
              <div class="bar bar-15"></div>
              <div class="bar bar-16"></div>
              <div class="bar bar-17"></div>
              <div class="bar bar-18"></div>
              <div class="bar bar-19"></div>
              <div class="bar bar-20"></div>
              <div class="bar bar-21"></div>
              <div class="bar bar-22"></div>
              <div class="bar bar-23"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<style scoped lang="scss">
$bar-slug: bar;
$line-slug: line;
$grey: #f5f5f5;
$darkgrey: #757575;

body {
  background: $grey;
}

.empty {
  font-family: verdana;
  text-align: center;
  width: 100%;
  h3 {
    font-size: 1.6em;
    font-weight: normal;
  }
}

.chart {
  //margin: 25px;
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 400px;
  overflow: hidden;
}

.no-chart-data {
  background: #fff;
  min-height: 400px;
  display: block;
  width: 100%;
  overflow: hidden;

  .title {
    color: $darkgrey;
    position: absolute;
    display: flex;
    left: 50%;
    bottom: 60%;
    margin-right: -50%;
    transform: translate(-50%, -100%);
    z-index: 1;
  }

  .y-axis,
  .x-axis {
    position: absolute;
    left: 25px;
    bottom: 25px;
  }

  .y-axis {
    height: 350px;
    width: 1px;
    border-left: 2px solid #f5f5f5;
  }

  .x-axis {
    height: 1px;
    width: 95%;
    border-bottom: 2px solid #f5f5f5;
  }

  .bars {
    position: absolute;
    display: inline-block;
    bottom: 15px;
    left: 35px;
    width: 100%;
    margin: 0 auto;
    //overflow: hidden;
    background: rgba(0, 0, 0, 0.1);

    .bar {
      background: $grey;
      display: inline-block;
      position: absolute;
      min-height: 5px;
      width: 3%;
      max-width: 25px;
      bottom: 10px;
      margin: 0 0.8%;
      border-top: 1px solid #fff;
      border-left: 1px solid #fff;
      border-right: 1px solid #fff;
      //display: none;

      @for $i from 0 to 23 {
        $height: random(350) + 10 + px;
        &.#{$bar-slug}-#{$i} {
          height: $height;
          display: inline-block;
          margin-left: 1% + $i * 4;
        }
      }
    }
  }

  .lines {
    position: absolute;
    display: inline-block;
    bottom: 15px;
    left: 35px;
    width: 100%;
    margin: 0 auto;
    //overflow: hidden;
    background: rgba(0, 0, 0, 0.1);

    .overlay {
      background: blue;
      display: block;
      width: 100%;
      height: inherit;
    }

    .line {
      background: $grey;
      display: inline-block;
      position: absolute;
      min-height: 15px;
      width: 14.28%;
      bottom: 10px;
      margin: 0 0.8%;
      border-top: 1px solid #fff;
      //border-left: 1px solid #FFF;
      //border-right: 1px solid #FFF;
      overflow: hidden;
      //display: none;

      @for $i from 0 to 7 {
        &.#{$line-slug}-#{$i} {
          margin-left: 1% + $i * 14.28%;
        }
      }

      .line-0 {
        height: 200px;
      }
      .line-1 {
        height: 400px;
      }

      .slope {
        display: block;
        background: white;
        width: 300%;
        height: 100px;
        margin-top: -50%;
        margin-left: -50%;

        &:nth-of-type(1) {
          -ms-transform: rotate(-25deg); /* IE 9 */
          -webkit-transform: rotate(-25deg); /* Chrome, Safari, Opera */
          transform: rotate(-25deg);
        }
      }
    }
  }

  .radial {
    text-align: center;
    .outer-circle {
      position: absolute;
      top: 50%;
      left: 50%;
      margin-top: -120px;
      margin-left: -150px;
      display: block;
      background: $grey;
      width: 300px;
      height: 300px;
      border-radius: 50%;
    }

    .inner-circle {
      position: absolute;
      top: 50%;
      left: 50%;
      margin-top: -100px;
      margin-left: -100px;
      display: block;
      background: #fff;
      width: 200px;
      height: 200px;
      border-radius: 50%;
    }
  }
}
</style>