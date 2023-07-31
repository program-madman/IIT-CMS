# CMS API接口文档

## 目录
- [接口概述](#接口概述)
  - [基本信息](#基本信息)
- [接口列表](#接口列表)
  - [1. 获取用户信息](#1-获取用户信息)
  - [2. 创建（注册）用户](#2-创建注册用户)
  - [3. 登陆](#3-登陆)
  - [4. 获取文章列表](#4-获取文章列表)
  - [5. 获取文章详情](#5-获取文章详情)
  - [6. 创建文章](#6-创建文章)
  - [7. 评论文章](#7-评论文章)
  - [8. 点赞文章](#8-点赞文章)
  - [9. 添加收藏](#9-添加收藏)
  - [10. 获取所有站内信](#10-获取所有站内信)
  - [11. 获取我发送的所有站内信](#11-获取我发送的所有站内信)
  - [12. 获取发送给我的所有站内信](#12-获取发送给我的所有站内信)
  - [13. 发送站内信给某个部门](#13-发送站内信给某个部门)
  - [14. 发送站内信给某个用户](#14-发送站内信给某个用户)
## 接口概述
该文档列出了CMS可用的前端API接口及其功能。

### 基本信息
- **本地URL**: `https://localhost:8080`
- **远程URL**: `https://81.70.89.185:8080`
- **请求格式**: HTTP/HTTPS

## 接口列表

### 1. 获取用户信息
获取特定用户的信息。

- **URL**: `/user/getUserInfoById`
- **方法**: POST
- **请求头**: token
- **请求体**:
  ```json
  {
  "userId": 100
  }

- **成功响应**:
    - **状态码**: 200 OK
    - **响应体**:
      ```json
      {
        "code": "200",
        "message": "Success!",
        "data": {
          "userId": 100,
          "deptId": 1,
          "roleId": 2,
          "firstName": "GuangHui",
          "lastName": "Liu",
          "username": null,
          "age": null,
          "phoneNumber": null,
          "houseNumber": null,
          "registerTime": "2023-07-27",
          "avatar": null,
          "userType": "Employee",
          "gender": "male",
          "country": null,
          "state": null,
          "city": null,
          "street": null,
          "mail": "345@123.com"
        }
      }
      ```

### 2. 创建（注册）用户
创建（注册）新用户。

- **URL**: `/user/register`
- **方法**: POST
- **请求头**: token
- **请求体**:
  ```json
  {
    "username": "user003",
    "deptId": 1,
    "roleId": 1,
    "password": "Zxcvbnm,./0",
    "passwordRepeat": "Zxcvbnm,./0",
    "firstName": "111",
    "lastName": "000",
    "userType": "Student",
    "gender": "Male",
    "mail": "123@gmail.com"
  }

- **成功响应**:
  - **状态码**: 200 OK
  - **响应体**:
    ```json
    {
      "code": "200",
      "message": "Success!",
      "data": true
    }
    ```
    
### 3. 登陆
用户使用用户名密码登陆系统。

- **URL**: `/user/login`
- **方法**: POST
- **请求头**: null
- **请求体**:
  ```json
  {
    "username": "ghliu@vw-mobvoi.com",
    "password": "Zxcvbnm,./0"
  }

- **成功响应**:
  - **状态码**: 200 OK
  - **响应体**:
    ```json
    {
      "code": "200",
      "message": "Success!",
      "data": {
          "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJJSVRfQ01TIiwiaWF0IjoxNjkwNzAyMzYwLCJleHAiOjE2OTMyOTQzNjAsIlVTRVIiOiJ7XCJ1c2VySWRcIjoxMDAsXCJkZXB0SWRcIjoxLFwicm9sZUlkXCI6MixcImZpcnN0TmFtZVwiOlwiR3VhbmdIdWlcIixcImxhc3ROYW1lXCI6XCJMaXVcIixcInBhc3N3b3JkXCI6XCI5N2Q2MWM4NWUxN2VhYzg4NzA5ZTEwNDRkMzExNjMyNzBmMjcxZjdmYmY3ZTI0MjBhZGVkM2U2ODI5NGQ3MjE4XCIsXCJyZWdpc3RlclRpbWVcIjpcIkp1bCAyNywgMjAyMyAxOjQwOjU3IEFNXCIsXCJ1c2VyVHlwZVwiOlwiRW1wbG95ZWVcIixcImdlbmRlclwiOlwibWFsZVwiLFwibWFpbFwiOlwiMzQ1QDEyMy5jb21cIixcImFkZHJlc3NJZFwiOjF9In0.u5KPDEYRDXMVwn7ZNXcvPmQOsSJkBdNP9h8jugEXBQU"
      }
    }
    ```
    
### 4. 获取文章列表
获取当前用户可以查看的全部文章列表。

- **URL**: `/article/getAllArticles`
- **方法**: POST
- **请求头**: token
- **请求体**: null
  ```json
  {

  }

- **成功响应**:
  - **状态码**: 200 OK
  - **响应体**:
    ```json
    {
      "code": "200",
      "message": "Success!",
      "data": [
          {
              "articleId": 99,
              "articleTitle": "Dynamic voltage scaling",
              "articleContent": "2023/7/26 23:51 Dynamic voltage scaling - Wikipedia https://en.wikipedia.org/wiki/Dynamic_voltage_scaling 1/4 Dynamic voltage scaling In computer architecture, dynamic voltage scaling is a power management technique in which the voltage used in a component is increased or decreased, depending upon circumstances. Dynamic voltage scaling to increase voltage is known as overvolting; dynamic voltage scaling to decrease voltage is known as undervolting. Undervolting is done in order to conserve power, particularly in laptops and other mobile devices, where energy comes from a battery and thus is limited, or in rare cases, to increase reliability. Overvolting is done in order to support higher frequencies for performance. The term \"overvolting\" is also used to refer to increasing static operating voltage of computer components to allow operation at higher speed (overclocking). MOSFET-based digital circuits operate using voltages at circuit nodes to represent logical state. The voltage at these nodes switches between a high voltage and a low voltage during normal operation—when the inputs to a logic gate transition, the transistors making up that gate may toggle the gate's output. Toggling a MOSFET's state requires changing its gate voltage from below the transistor's threshold voltage to above it (or from above it to below it). However, changing the gate's voltage requires charging or discharging the capacitance at its node. This capacitance is the sum of capacitances from various sources: primarily transistor gate capacitance, diffusion capacitance, and wires (coupling capacitance). Higher supply voltages result in faster slew rate (rate of change of voltage per unit of time) when charging and discharging, which allows for quicker transitioning through the MOSFET's threshold voltage. Additionally, the more the gate voltage exceeds the threshold voltage, the lower the resistance of the transistor's conducting channel. This results in a lower RC time constant for quicker charging and discharging of the capacitance of the subsequent logic stage. Quicker transitioning afforded by higher supply voltages allows for operating at higher frequencies. Many modern components allow voltage regulation to be controlled through software (for example, through the BIOS). It is usually possible to control the voltages supplied to the CPU, RAM, PCI, and PCI Express (or AGP) port through a PC's BIOS. However, some components do not allow software control of supply voltages, and hardware modification is required by overclockers seeking to overvolt the component for extreme overclocks. Video cards and motherboard northbridges are components which frequently require hardware modifications to change supply voltages. These modifications are known as \"voltage mods\" or \"Vmod\" in the overclocking community. Background Methods Undervolting 2023/7/26 23:51 Dynamic voltage scaling - Wikipedia https://en.wikipedia.org/wiki/Dynamic_voltage_scaling 2/4 Undervolting is reducing the voltage of a component, usually the processor, reducing temperature and cooling requirements, and possibly allowing a fan to be omitted. Just like overclocking, undervolting is highly subject to the so-called silicon lottery: one CPU can undervolt slightly better than the other and vice versa. The switching power dissipated by a chip using static CMOS gates is , where is the capacitance being switched per clock cycle, is the supply voltage, is the switching frequency,[1] and is the activity factor. Since is squared, this part of the power consumption decreases quadratically with voltage. The formula is not exact however, as many modern chips are not implemented using 100% CMOS, but also use special memory circuits, dynamic logic such as domino logic, etc. Moreover, there is also a static leakage current, which has become more and more accentuated as feature sizes have become smaller (below 90 nanometres) and threshold levels lower. Accordingly, dynamic voltage scaling is widely used as part of strategies to manage switching power consumption in battery powered devices such as cell phones and laptop computers. Low voltage modes are used in conjunction with lowered clock frequencies to minimize power consumption associated with components such as CPUs and DSPs; only when significant computational power is needed will the voltage and frequency be raised. Some peripherals also support low voltage operational modes. For example, low power MMC and SD cards can run at 1.8 V as well as at 3.3 V, and driver stacks may conserve power by switching to the lower voltage after detecting a card which supports it. When leakage current is a significant factor in terms of power consumption, chips are often designed so that portions of them can be powered completely off. This is not usually viewed as being dynamic voltage scaling, because it is not transparent to software. When sections of chips can be turned off, as for example on TI OMAP3 processors, drivers and other support software need to support that. The speed at which a digital circuit can switch states - that is, to go from \"low\" (VSS) to \"high\" (VDD) or vice versa - is proportional to the voltage differential in that circuit. Reducing the voltage means that circuits switch slower, reducing the maximum frequency at which that circuit can run. This, in turn, reduces the rate at which program instructions that can be issued, which may increase run time for program segments which are sufficiently CPU-bound. This again highlights why dynamic voltage scaling is generally done in conjunction with dynamic frequency scaling, at least for CPUs. There are complex tradeoffs to consider, which depend on the particular system, the load presented to it, and power management goals. When quick responses are needed (e.g. Mobile Sensors and Context-Aware Computing), clocks and voltages might be raised together. Otherwise, they may both be kept low to maximize battery life. The 167-processor AsAP 2 chip enables individual processors to make extremely fast (on the order of 1-2ns) and locally controlled changes to their own supply voltages. Processors connect their local power grid to either a higher (VddHi) or lower (VddLow) supply voltage, or can be cut off entirely from either grid to dramatically cut leakage power. Power Program execution speed Implementations 2023/7/26 23:51 Dynamic voltage scaling - Wikipedia https://en.wikipedia.org/wiki/Dynamic_voltage_scaling 3/4 Another approach uses per-core on-chip switching regulators for dynamic voltage and frequency scaling (DVFS).[2] Unix system provides a userspace governor, allowing to modify the cpu frequencies (though limited to hardware capabilities). Dynamic frequency scaling is another power conservation technique that works on the same principles as dynamic voltage scaling. Both dynamic voltage scaling and dynamic frequency scaling can be used to prevent computer system overheating, which can result in program or operating system crashes, and possibly hardware damage. Reducing the voltage supplied to the CPU below the manufacturer's recommended minimum setting can result in system instability. The efficiency of some electrical components, such as voltage regulators, decreases with increasing temperature, so the power used may increase with temperature causing thermal runaway. Increases in voltage or frequency may increase system power demands even faster than the CMOS formula indicates, and vice versa.[3][4] The primary caveat of overvolting is increased heat: the power dissipated by a circuit increases with the square of the voltage applied, so even small voltage increases significantly affect power. At higher temperatures, transistor performance is adversely affected, and at some threshold, the performance reduction due to the heat exceeds the potential gains from the higher voltages. Overheating and damage to circuits can occur very quickly when using high voltages. There are also longer-term concerns: various adverse device-level effects such as hot carrier injection and electromigration occur more rapidly at higher voltages, decreasing the lifespan of overvolted components. Dynamic voltage and frequency scaling (DVFS) Dynamic frequency scaling Power gating Power–delay product (PDP) Energy–delay product (EDP) Switched-mode power supply applications (SMPS) applications Switching energy Power ramp Overvoltage Undervoltage Operating system API System stability Temperature Caveats See also 2023/7/26 23:51 Dynamic voltage scaling - Wikipedia https://en.wikipedia.org/wiki/Dynamic_voltage_scaling 4/4 Voltage optimization 1. J. M. Rabaey. Digital Integrated Circuits. Prentice Hall, 1996. 2. Wonyoung Kim, Meeta S. Gupta, Gu-Yeon Wei and David Brooks. \"System Level Analysis of Fast, Per-Core DVFS using On-Chip Switching Regulators\" (http://www.eecs.harvard.e du/~meeta/my_papers/hpca08_onchipvrm.pdf). 2008. 3. Mike Chin. \"Asus EN9600GT Silent Edition Graphics Card\" (http://www.silentpcreview.co m/article821-page5.html). Silent PC Review. p. 5. Retrieved 2008-04-21. 4. Mike Chin. \"80 Plus expands podium for Bronze, Silver & Gold\" (http://www.silentpcrevie w.com/article814-page1.html). Silent PC Review. Retrieved 2008-04-21. Gaudet, Vincent C. (2014-04-01) [2013-09-25]. \"Chapter 4.1. Low-Power Design Techniques for State-of-the-Art CMOS Technologies\". In Steinbach, Bernd [in German] (ed.). Recent Progress in the Boolean Domain (https://books.google.com/books?id=_pwx BwAAQBAJ) (1 ed.). Newcastle upon Tyne, UK: Cambridge Scholars Publishing. pp. 187– 212. ISBN 978-1-4438-5638-6. Retrieved 2019-08-04. [1] (https://m.tau.ac.il/~ilia1/publi cations/rpbd_book.pdf) (455 pages) Retrieved from \"https://en.wikipedia.org/w/index.php?title=Dynamic_voltage_scaling&oldid=1154706755\" References Further reading",
              "publishTime": "2023-07-25 11:58:33.0",
              "authorDeptName": null,
              "articleCategory": "Technology",
              "attachmentTotalCount": 0,
              "likes": 0,
              "isFav": false,
              "isRead": false
          },
          {
              "articleId": 100,
              "articleTitle": "Overvoltage",
              "articleContent": "2023/7/26 23:51 Overvoltage - Wikipedia https://en.wikipedia.org/wiki/Overvoltage 1/3 Voltage spike. Lack of 3-phase electric system connected by star. If neutral breaks off, small-power appliances will be destroyed by overvoltage Overvoltage In electrical engineering, overvoltage is the raising of voltage beyond the design limit of a circuit or circuit element. The conditions may be hazardous. Depending on its duration, the overvoltage event can be transient—a voltage spike—or permanent, leading to a power surge. Electronic and electrical devices are designed to operate at a certain maximum supply voltage, and considerable damage can be caused by voltage that is higher than that for which the devices are rated. For example, an electric light bulb has a wire in it that at the given rated voltage will carry a current just large enough for the wire to get very hot (giving off light and heat), but not hot enough for it to melt. The amount of current in a circuit depends on the voltage supplied: if the voltage is too high, then the wire may melt and the light bulb would have \"burned out real time\". Similarly other electrical devices may stop working, or may even burst into flames if an overvoltage is delivered to the circuit. A typical natural source of transient overvoltage events is lightning. Bursts of solar wind following solar flares are also known to cause overvoltage in electrical circuits, especially onboard space satellites. Man-made sources of spikes are usually caused by electromagnetic induction when switching on or off inductive loads (such as electric motors or electromagnets), or by switching heavy resistive AC loads when zero-crossing circuitry is not used - anywhere a large change of current takes place. One of the purposes of electromagnetic compatibility compliance is to eliminate such sources. An important potential source of dangerous overvoltage is electronic warfare. There is intensive military research in this field, whose goal is to produce various transient electromagnetic devices designed to generate electromagnetic pulses that will disable an enemy's electronic equipment. A recent military development is that of the exploding capacitor designed to radiate a high voltage electromagnetic pulse. Another intense source of an electromagnetic pulse is a nuclear explosion. Explanation Sources Natural Man-made 2023/7/26 23:51 Overvoltage - Wikipedia https://en.wikipedia.org/wiki/Overvoltage 2/3 Diesel or petrol powered generators can also be a source of overvoltage. If the generator speed increases suddenly, such as due to inadvertent throttle movement or unstable operation of the prime mover, this can cause brief but damaging overvoltage to any connected equipment. A faulty voltage regulator can also cause overvoltage.[1][2] An example of an overvoltage event occurred on August 25, 2011 in East Palo Alto, California. The line voltage supplied to 232 customers increased from 110  volts to around 400  volts for 80 minutes. The event damaged electronic equipment, light bulbs, and other electrical equipment including smart meters. [3][4] The opposite of such an event is a brownout. The transient pulses can get into the equipment either by power or data lines, or directly through space from a strong electromagnetic field change - an electromagnetic pulse (EMP). Filters are used to prevent spikes entering or leaving the equipment through wires, and the devices coupled electromagnetically to space (such as radio-frequency pick-up coils in MRI scanners) are protected by shielding. Arcing horns Avalanche diode Gas-filled tube Lightning rod Metal-oxide varistor Spark gap Transient-voltage-suppression diode Trisil Zener diode Energy portal Crowbar (circuit) Electrical fault Electronic countermeasure Electrostatic discharge Overcurrent Overvolting Undervoltage-lockout Conduction path Overvoltage protection devices See also 2023/7/26 23:51 Overvoltage - Wikipedia https://en.wikipedia.org/wiki/Overvoltage 3/3 Uninterruptible power supply 1. \"Generator Over Voltage – What Possible Cause?\" (https://powercontinuity.co.uk/knowle dge-base/generator-over-voltage-what-possible-cause). Power Continuity. 2. \"What Causes High Voltage in Diesel Generators? What Can We do to Prevent Generator from Being over Voltage?-Tide Power Technology Co., LTD\" (https://www.tidepower.uk/n ews/428.html). 3. \"Power surge raises questions about SmartMeters\" (https://www.paloaltoonline.com/new s/2011/09/04/power-surge-raises-questions-about-smartmeters). 4. \"Power surge pops lights, televisions in East Palo Alto\" (https://www.paloaltoonline.com/ news/2011/08/25/power-surge-pops-lights-televisions-in-east-palo-alto). Overvoltage protection (http://www.piclist.com/techref/power/overvolt.htm) What Is Overvoltage? Definition, Causes, Protection, Categories (https://www.asutpp.co m/overvoltage-protection) Retrieved from \"https://en.wikipedia.org/w/index.php?title=Overvoltage&oldid=1159576119\" References External links",
              "publishTime": "2023-07-25 11:58:33.0",
              "authorDeptName": null,
              "articleCategory": "Technology",
              "attachmentTotalCount": 1,
              "likes": 1,
              "isFav": false,
              "isRead": true
          }
      ]
    }
    ```
    
### 5. 获取文章详情
根据文章ID，获取文章详情。

- **URL**: `/article/getArticleById`
- **方法**: POST
- **请求头**: token
- **请求体**: 
  ```json
  {
    "articleId": 100
  }

- **成功响应**:
  - **状态码**: 200 OK
  - **响应体**:
    ```json
    {
      "code": "200",
      "message": "Success!",
      "data": {
          "articleId": 100,
          "articleTitle": "Overvoltage",
          "articleContent": "2023/7/26 23:51 Overvoltage - Wikipedia https://en.wikipedia.org/wiki/Overvoltage 1/3 Voltage spike. Lack of 3-phase electric system connected by star. If neutral breaks off, small-power appliances will be destroyed by overvoltage Overvoltage In electrical engineering, overvoltage is the raising of voltage beyond the design limit of a circuit or circuit element. The conditions may be hazardous. Depending on its duration, the overvoltage event can be transient—a voltage spike—or permanent, leading to a power surge. Electronic and electrical devices are designed to operate at a certain maximum supply voltage, and considerable damage can be caused by voltage that is higher than that for which the devices are rated. For example, an electric light bulb has a wire in it that at the given rated voltage will carry a current just large enough for the wire to get very hot (giving off light and heat), but not hot enough for it to melt. The amount of current in a circuit depends on the voltage supplied: if the voltage is too high, then the wire may melt and the light bulb would have \"burned out real time\". Similarly other electrical devices may stop working, or may even burst into flames if an overvoltage is delivered to the circuit. A typical natural source of transient overvoltage events is lightning. Bursts of solar wind following solar flares are also known to cause overvoltage in electrical circuits, especially onboard space satellites. Man-made sources of spikes are usually caused by electromagnetic induction when switching on or off inductive loads (such as electric motors or electromagnets), or by switching heavy resistive AC loads when zero-crossing circuitry is not used - anywhere a large change of current takes place. One of the purposes of electromagnetic compatibility compliance is to eliminate such sources. An important potential source of dangerous overvoltage is electronic warfare. There is intensive military research in this field, whose goal is to produce various transient electromagnetic devices designed to generate electromagnetic pulses that will disable an enemy's electronic equipment. A recent military development is that of the exploding capacitor designed to radiate a high voltage electromagnetic pulse. Another intense source of an electromagnetic pulse is a nuclear explosion. Explanation Sources Natural Man-made 2023/7/26 23:51 Overvoltage - Wikipedia https://en.wikipedia.org/wiki/Overvoltage 2/3 Diesel or petrol powered generators can also be a source of overvoltage. If the generator speed increases suddenly, such as due to inadvertent throttle movement or unstable operation of the prime mover, this can cause brief but damaging overvoltage to any connected equipment. A faulty voltage regulator can also cause overvoltage.[1][2] An example of an overvoltage event occurred on August 25, 2011 in East Palo Alto, California. The line voltage supplied to 232 customers increased from 110  volts to around 400  volts for 80 minutes. The event damaged electronic equipment, light bulbs, and other electrical equipment including smart meters. [3][4] The opposite of such an event is a brownout. The transient pulses can get into the equipment either by power or data lines, or directly through space from a strong electromagnetic field change - an electromagnetic pulse (EMP). Filters are used to prevent spikes entering or leaving the equipment through wires, and the devices coupled electromagnetically to space (such as radio-frequency pick-up coils in MRI scanners) are protected by shielding. Arcing horns Avalanche diode Gas-filled tube Lightning rod Metal-oxide varistor Spark gap Transient-voltage-suppression diode Trisil Zener diode Energy portal Crowbar (circuit) Electrical fault Electronic countermeasure Electrostatic discharge Overcurrent Overvolting Undervoltage-lockout Conduction path Overvoltage protection devices See also 2023/7/26 23:51 Overvoltage - Wikipedia https://en.wikipedia.org/wiki/Overvoltage 3/3 Uninterruptible power supply 1. \"Generator Over Voltage – What Possible Cause?\" (https://powercontinuity.co.uk/knowle dge-base/generator-over-voltage-what-possible-cause). Power Continuity. 2. \"What Causes High Voltage in Diesel Generators? What Can We do to Prevent Generator from Being over Voltage?-Tide Power Technology Co., LTD\" (https://www.tidepower.uk/n ews/428.html). 3. \"Power surge raises questions about SmartMeters\" (https://www.paloaltoonline.com/new s/2011/09/04/power-surge-raises-questions-about-smartmeters). 4. \"Power surge pops lights, televisions in East Palo Alto\" (https://www.paloaltoonline.com/ news/2011/08/25/power-surge-pops-lights-televisions-in-east-palo-alto). Overvoltage protection (http://www.piclist.com/techref/power/overvolt.htm) What Is Overvoltage? Definition, Causes, Protection, Categories (https://www.asutpp.co m/overvoltage-protection) Retrieved from \"https://en.wikipedia.org/w/index.php?title=Overvoltage&oldid=1159576119\" References External links",
          "authorName": "GuangHui Liu",
          "publishTime": "2023-07-25 11:58:33",
          "readCount": "1",
          "replyCount": "2",
          "likeCount": "1",
          "browseCount": "1",
          "targetDeptName": [
            "Business Administration"
          ],
          "changeHistoryResponseList": null,
          "attachmentList": null,
          "commentList": [
              {
                  "commentId": 99,
                  "commenterName": "GuangHui Liu",
                  "commenterDept": "Computer Science",
                  "commenterType": "Employee",
                  "commentTime": "2023-07-29T18:04:57.000+00:00"
              },
              {
                  "commentId": 100,
                  "commenterName": "GuangHui Liu",
                  "commenterDept": "Computer Science",
                  "commenterType": "Employee",
                  "commentTime": "2023-07-29T18:56:06.000+00:00"
              }
          ]
      }
    }
    ```

### 6. 创建文章
创建文章。

- **URL**: `/article/createArticle`
- **方法**: POST
- **请求头**: token
- **请求体**: 
  ```json
  {
    "title": "002 test creawte article",
    "content": "this is a test article",
    "categoryName": "Technology",
    "attachmentList": [],
    "targetDeptList": [],
    "userTypeList": [],
    "categoryList": []
  }

- **成功响应**:
  - **状态码**: 200 OK
  - **响应体**:
    ```json
    {
      "code": "200",
      "message": "Success!",
      "data": null
      }
    ```

### 7. 评论文章
在某一个文章下面进行评论。

- **URL**: `/article/addComment`
- **方法**: POST
- **请求头**: token
- **请求体**: 
  ```json
  {
    "articleId": 100,
    "content": "this is a comment2 from same user"
  }

- **成功响应**:
  - **状态码**: 200 OK
  - **响应体**:
    ```json
    {
      "code": "200",
      "message": "Success!",
      "data": null
      }
    ```

### 8. 点赞文章
给某一个文章点赞，已赞过的再次调用此接口进行取消点赞操作。

- **URL**: `/article/likeArticle`
- **方法**: POST
- **请求头**: token
- **请求体**: 
  ```json
  {
    "articleId": 100
  }

- **成功响应**:
  - **状态码**: 200 OK
  - **响应体**:
    ```json
    {
      "code": "200",
      "message": "Success!",
      "data": null
      }
    ```

### 9. 添加收藏
收藏一篇文章，，已收藏过的再次调用此接口进行取消收藏操作。

- **URL**: `/article/likeArticle`
- **方法**: POST
- **请求头**: token
- **请求体**: 
  ```json
  {
    "articleId": 100
  }

- **成功响应**:
  - **状态码**: 200 OK
  - **响应体**:
    ```json
    {
      "code": "200",
      "message": "Success!",
      "data": null
      }
    ```

### 10. 获取所有站内信
获取所有站内信。

- **URL**: `/message/getAllMessages`
- **方法**: POST
- **请求头**: token
- **请求体**: null
  ```json
  {
    
  }

- **成功响应**:
  - **状态码**: 200 OK
  - **响应体**:
    ```json
    {
    "code": "200",
    "message": "Success!",
    "data": [
        {
            "messageId": 1,
            "fromUser": 100,
            "toUser": 101,
            "title": "Test Message",
            "content": "Hello world",
            "sendTime": "2023-07-24T15:30:58.000+00:00"
        },
        {
            "messageId": 2,
            "fromUser": 100,
            "toUser": 101,
            "title": "Test Message",
            "content": "Hello world",
            "sendTime": "2023-07-24T15:30:58.000+00:00"
        }
      ]
    }
    ```
    
### 11. 获取我发送的所有站内信
获取当前登陆账号发送的全部站内信。

- **URL**: `/message/getAllMessagesSendByMe`
- **方法**: POST
- **请求头**: token
- **请求体**: null
  ```json
  {
    
  }

- **成功响应**:
  - **状态码**: 200 OK
  - **响应体**:
    ```json
    {
    "code": "200",
    "message": "Success!",
    "data": [
        {
            "messageId": 1,
            "fromUser": 100,
            "toUser": 101,
            "title": "Test Message",
            "content": "Hello world",
            "sendTime": "2023-07-24T15:30:58.000+00:00"
        },
        {
            "messageId": 2,
            "fromUser": 100,
            "toUser": 101,
            "title": "Test Message",
            "content": "Hello world",
            "sendTime": "2023-07-24T15:30:58.000+00:00"
        }
      ]
    }
    ```
    
### 12. 获取发送给我的所有站内信
获取其他账号发送给当前登陆账号的全部站内信。

- **URL**: `/message/getAllMessagesSendToMe`
- **方法**: POST
- **请求头**: token
- **请求体**: null
  ```json
  {
    
  }

- **成功响应**:
  - **状态码**: 200 OK
  - **响应体**:
    ```json
    {
    "code": "200",
    "message": "Success!",
    "data": [
         {
            "messageId": 101,
            "fromUser": 100,
            "toUser": 100,
            "title": "message title",
            "content": "message content",
            "sendTime": "2023-07-31T09:23:34.000+00:00"
        }
      ]
    }
    ```


### 13. 发送站内信给某个部门
指定部门ID发送站内信给某个部门。

- **URL**: `/message/sendMessageToDept`
- **方法**: POST
- **请求头**: token
- **请求体**: 
  ```json
  {
    "toDeptId": 2,
    "title": "message title",
    "content": "message content"
  }

- **成功响应**:
  - **状态码**: 200 OK
  - **响应体**:
    ```json
    {
      "code": "200",
      "message": "Success!",
      "data": null
    }
    ```


### 14. 发送站内信给某个用户
指定用户ID发送站内信。

- **URL**: `/message/sendMessageToUser`
- **方法**: POST
- **请求头**: token
- **请求体**: 
  ```json
  {
    "toUserId": 100,
    "title": "message title",
    "content": "message content"
  }

- **成功响应**:
  - **状态码**: 200 OK
  - **响应体**:
    ```json
    {
      "code": "200",
      "message": "Success!",
      "data": null
    }
    ```

