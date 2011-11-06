/**************************************************************
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 *************************************************************/


#ifndef CONNECTIVITY_POOLEDCONNECTION_HXX
#define CONNECTIVITY_POOLEDCONNECTION_HXX

#include <cppuhelper/compbase2.hxx>
#include <com/sun/star/sdbc/XPooledConnection.hpp>
#include <com/sun/star/lang/XEventListener.hpp>
#include <comphelper/broadcasthelper.hxx>
#include <com/sun/star/reflection/XProxyFactory.hpp>


namespace connectivity
{
	//==========================================================================
	//= OPooledConnection - 
	//= allows to pool a real connection
	//==========================================================================
	typedef	::cppu::WeakComponentImplHelper2<	 ::com::sun::star::sdbc::XPooledConnection
												,::com::sun::star::lang::XEventListener>	OPooledConnection_Base;

	class OPooledConnection : public ::comphelper::OBaseMutex
							 ,public OPooledConnection_Base
	{
		::com::sun::star::uno::Reference< ::com::sun::star::sdbc::XConnection >				m_xRealConnection;	// the connection fom driver
		::com::sun::star::uno::Reference< ::com::sun::star::lang::XComponent >				m_xComponent;		// the connection which wraps the real connection
		::com::sun::star::uno::Reference< ::com::sun::star::reflection::XProxyFactory >		m_xProxyFactory;
	public:
		// OComponentHelper
		virtual void SAL_CALL disposing(void);

		OPooledConnection(const ::com::sun::star::uno::Reference< ::com::sun::star::sdbc::XConnection >& _xConnection,
						  const ::com::sun::star::uno::Reference< ::com::sun::star::reflection::XProxyFactory >& _rxProxyFactory);

		//XPooledConnection 
		virtual ::com::sun::star::uno::Reference< ::com::sun::star::sdbc::XConnection > SAL_CALL getConnection(  ) throw (::com::sun::star::sdbc::SQLException, ::com::sun::star::uno::RuntimeException);

		// XEventListener
		virtual void SAL_CALL disposing( const ::com::sun::star::lang::EventObject& Source ) throw (::com::sun::star::uno::RuntimeException);
	};

}
#endif // CONNECTIVITY_POOLEDCONNECTION_HXX

