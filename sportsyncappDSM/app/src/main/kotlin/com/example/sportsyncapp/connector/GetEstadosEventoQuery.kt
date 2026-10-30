
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


public interface GetEstadosEventoQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      DefaultConnector,
      GetEstadosEventoQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val estadosEventos: List<EstadosEventosItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class EstadosEventosItem(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreEstado: String?,
  
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "GetEstadosEvento"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun GetEstadosEventoQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    GetEstadosEventoQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun GetEstadosEventoQuery.execute(

  

  ): com.google.firebase.dataconnect.QueryResult<
    GetEstadosEventoQuery.Data,
    Unit
  > =
  ref(
    
  ).execute()


  public fun GetEstadosEventoQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<GetEstadosEventoQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

