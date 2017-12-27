四种数据库连接池(druid、c3p0、dbcp、proxool)配置文件密码加密处理方法

如何配置数据库密码加密访问数据库 ？
将配置文件用户相关的信息(例如：密码)进行加密使其以密文形式存在，进行初始化连接池的时候进行解密操作，达到成功创建连接池的目的。
配置数据库属性文件, 配置数据库连接的密码（jdbc.password）设置为加密后的值，该值可以采用AES、DES、3DES等对称加密方式实现，也可以采用RSA的加密算法存储。

	<!-- 配置系统的数据源DBCP -->
	<!-- 
	<bean id="dataSource" class="xyz.dongxiaoxia.commons.persistence.uya.datasource.DBCPBasicDataSource"
		destroy-method="close">
		<property name="driverClassName" value="${jdbc.driver}" />
		<property name="url" value="${jdbc.url}" />
		<property name="username" value="${jdbc.username}" />
		<property name="password" value="${jdbc.password}" />
		< Connection Pooling Info >
		<property name="initialSize" value="10" />
		<property name="maxActive" value="50" />
		<property name="maxIdle" value="20" />
		<property name="minIdle" value="5" />
		<property name="maxWait" value="60000" />
		<property name="poolPreparedStatements" value="true" />
		<property name="defaultAutoCommit" value="false" />
		<property name="logAbandoned" value="true" />
		<property name="removeAbandoned" value="true" />
		<property name="removeAbandonedTimeout" value="120" />
		< 打开检查,用异步线程evict进行检查 >
		<property name="testWhileIdle" value="true" />
		<property name="validationQuery">
			<value>select count(*) from T_SYS_USER</value>
		</property>
		<property name="validationQueryTimeout" value="1" />
		<property name="timeBetweenEvictionRunsMillis" value="30000" />
		<property name="numTestsPerEvictionRun" value="50" />
	</bean>
	 -->

	<!--配置系统的数据源C3P0-->
	<!-- <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource"
		destroy-method="close">
		<property name="driverClass" value="${jdbc.driver}" />
		<property name="jdbcUrl" value="${jdbc.url}" />
		<property name="properties" ref="dataSourceProperties"/> 
		<property name="acquireIncrement" value="1"/>  
        <property name="idleConnectionTestPeriod" value="300"/>  
        <property name="maxPoolSize" value="50"/>  
        <property name="minPoolSize" value="10"/>  
        <property name="initialPoolSize" value="10" />  
        <property name="numHelperThreads" value="3"/>  
        <property name="maxIdleTime" value="1200" />  
        <property name="acquireRetryAttempts" value="2"/>  
        <property name="preferredTestQuery" value="select count(*) from T_SYS_USER"/>  
        <property name="testConnectionOnCheckin" value="true"/>
	</bean>
	
	<bean id="dataSourceProperties" class="xyz.dongxiaoxia.commons.persistence.uya.datasource.C3P0FactoryBean">  
        <property name="properties">  
            <props>  
            	<prop key="user">${jdbc.username}</prop>
				<prop key="password">${jdbc.password}</prop>
            </props>  
        </property>  
    </bean> -->
	<!--End配置系统的数据源C3P0-->

	<!--配置系统的数据源Proxool-->
	<bean id="dataSource" class="xyz.dongxiaoxia.commons.persistence.uya.datasource.DefaultProxoolDataSource">
		<property name="driver" value="${jdbc.driver}"/>
		<property name="driverUrl" value="${jdbc.url}" />
		<property name="user" value="${jdbc.username}" />
		<property name="password" value="${jdbc.password}" />
		<property name="alias" value="Pool_dbname" />
		<property name="prototypeCount" value="0" />
		<property name="maximumConnectionCount" value="50" />
		<property name="minimumConnectionCount" value="10" />
		<property name="simultaneousBuildThrottle" value="50" />
		<property name="houseKeepingTestSql" value="select count(*) from T_SYS_USER" />
	</bean>
	<!--End配置系统的数据源Proxool-->
	
	
    <!--配置系统的数据源Druid-->
	<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
            <property name="driverClassName" value="${driver}" />
            <property name="url" value="${url}" />
            <property name="username" value="${username}" />
            <property name="password" value="${password}" />
            <!-- config.decrypt=true -->
            <property name="filters" value="config" />
            <property name="connectionProperties" value="config.decrypt=true" />
            <!-- 初始化连接大小 -->
            <property name="initialSize" value="${initialSize}" />
            <!-- 连接池最大使用连接数量 -->
            <property name="maxActive" value="${maxActive}" />
            <!-- 连接池最大空闲 这个参数已经被弃用 -->
            <property name="maxIdle" value="${maxIdle}"></property>
            <!-- 连接池最小空闲 -->
            <property name="minIdle" value="${minIdle}"></property>
            <!-- 获取连接最大等待时间 -->
            <property name="maxWait" value="${maxWait}"></property>
            <property name="validationQuery" value="${validationQuery}" />
            <property name="testWhileIdle" value="${testWhileIdle}" />
            <property name="testOnBorrow" value="${testOnBorrow}" />
            <property name="testOnReturn" value="${testOnReturn}" />
            <!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
            <property name="timeBetweenEvictionRunsMillis" value="${timeBetweenEvictionRunsMillis}" />
            <!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
            <property name="minEvictableIdleTimeMillis" value="${minEvictableIdleTimeMillis}" />
            <!-- 关闭长时间不使用的连接 打开removeAbandoned功能 -->
            <property name="removeAbandoned" value="${removeAbandoned}" />
            <!-- 1200秒，也就是20分钟 -->
            <property name="removeAbandonedTimeout" value="${removeAbandonedTimeout}" />
            <!-- 关闭abanded连接时输出错误日志 -->
            <property name="logAbandoned" value="${logAbandoned}" />
        </bean>
        <!--End配置系统的数据源Druid-->