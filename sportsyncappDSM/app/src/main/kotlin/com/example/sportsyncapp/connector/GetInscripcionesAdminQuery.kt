
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


public interface GetInscripcionesAdminQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      DefaultConnector,
      GetInscripcionesAdminQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val usuarios: Usuarios?,
  
    val inscripcioness: List<InscripcionessItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class Usuarios(
  
    val rol: Rol,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class Rol(
  
    val nombreRol: String,
  
  ) {
    
    
  }
      
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class InscripcionessItem(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val fechaInscripcion: com.google.firebase.dataconnect.LocalDate,
  
    val evento: Evento,
  
    val equipo: Equipo?,
  
    val perfil: Perfil?,
  
    val usuarioSolicitante: UsuarioSolicitante,
  
    val usuarioRevisor: UsuarioRevisor?,
  
    val estado: Estado,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class Evento(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombre: String,
  
    val fecha: com.google.firebase.dataconnect.LocalDate,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class Equipo(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreEquipo: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class Perfil(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreDeportivo: String?,
  
    val usuario: Usuario?,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class Usuario(
  
    val id: String,
  
    val nombre: String,
  
    val apellido: String,
  
  ) {
    
    
  }
      
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class UsuarioSolicitante(
  
    val id: String,
  
    val nombre: String,
  
    val apellido: String,
  
    val correo: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class UsuarioRevisor(
  
    val id: String,
  
    val nombre: String,
  
    val apellido: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class Estado(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreEstado: String?,
  
  ) {
    
    
  }
      
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "GetInscripcionesAdmin"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun GetInscripcionesAdminQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    GetInscripcionesAdminQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun GetInscripcionesAdminQuery.execute(

  

  ): com.google.firebase.dataconnect.QueryResult<
    GetInscripcionesAdminQuery.Data,
    Unit
  > =
  ref(
    
  ).execute()


  public fun GetInscripcionesAdminQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<GetInscripcionesAdminQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

