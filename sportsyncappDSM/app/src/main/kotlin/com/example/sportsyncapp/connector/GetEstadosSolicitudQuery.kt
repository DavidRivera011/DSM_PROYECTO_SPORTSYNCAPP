
@file:Suppress(
  "KotlinRedundantDiagnosticSuppress",
  "PropertyName",
  "MayBeConstant",
  "RedundantVisibilityModifier",
  "RedundantCompanionReference",
  "RemoveEmptyClassBody",
  "SpellCheckingInspection",
  "unused",
)

package com.example.sportsyncapp.connector


import kotlinx.coroutines.flow.filterNotNull as _flow_filterNotNull
import kotlinx.coroutines.flow.map as _flow_map


public interface GetEstadosSolicitudQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      DefaultConnector,
      GetEstadosSolicitudQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val estadosSolicitudess: List<EstadosSolicitudessItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class EstadosSolicitudessItem(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreEstado: String,
  
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "GetEstadosSolicitud"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun GetEstadosSolicitudQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    GetEstadosSolicitudQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun GetEstadosSolicitudQuery.execute(

  

  ): com.google.firebase.dataconnect.QueryResult<
    GetEstadosSolicitudQuery.Data,
    Unit
  > =
  ref(
    
  ).execute()


  public fun GetEstadosSolicitudQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<GetEstadosSolicitudQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

